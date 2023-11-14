package com.ppn.ppn.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.DescribeLogStreamsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.DescribeLogStreamsResponse;
import software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent;
import software.amazon.awssdk.services.cloudwatchlogs.model.PutLogEventsRequest;

import java.util.LinkedList;
import java.util.Queue;

import static software.amazon.awssdk.regions.Region.US_EAST_1;

public class CloudWatchAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private CloudWatchLogsClient client;
    private String logGroupName = "ppn-project-logs";
    private String logStreamName = "prod-log-app-stream";

    private Queue<InputLogEvent> eventQueue;

    public CloudWatchAppender() {
        client = CloudWatchLogsClient
                .builder()
                .region(US_EAST_1)
                .build();
        eventQueue = new LinkedList<>();
    }

    @Override
    protected void append(ILoggingEvent event) {
        InputLogEvent logEvent = InputLogEvent
                .builder()
                .message(event.getLevel().levelStr + " " + event.getFormattedMessage())
                .timestamp(event.getTimeStamp()).build();

        eventQueue.add(logEvent);
        flushEvents();
    }

    public void flushEvents() {
        // Retrieve the existing log events
        DescribeLogStreamsResponse describeLogStreamsResponse = client
                .describeLogStreams(DescribeLogStreamsRequest.builder().logGroupName(logGroupName)
                        .logStreamNamePrefix(logStreamName)
                        .build());

        String sequenceToken = describeLogStreamsResponse.logStreams().get(0).uploadSequenceToken();

        // Batch up the next 10 events
        LinkedList<InputLogEvent> logEventsBatch = new LinkedList<>();
        while (!eventQueue.isEmpty() && logEventsBatch.size() < 10) {
            logEventsBatch.add(eventQueue.poll());
        }

        // Check if logEventsBatch is empty
        if (logEventsBatch.isEmpty()) {
            return;
        }

        // Put the log events into the CloudWatch stream
        PutLogEventsRequest putLogEventsRequest = PutLogEventsRequest
                .builder()
                .logGroupName(logGroupName)
                .logStreamName(logStreamName)
                .logEvents(logEventsBatch)
                .sequenceToken(sequenceToken).build();

        client.putLogEvents(putLogEventsRequest);
    }

    @Override
    public void stop() {
        // Flush any remaining events before stopping
        flushEvents();

        // Clean up the AWS CloudWatchLogs client
        client.close();
        super.stop();
    }
}
