package com.ppn.ppn.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class APIResponse<T> {
    private String message;
    private LocalDateTime timeStamp;
    private Boolean isSuccess;
    private Integer statusCode;
    private T data;

    public APIResponse(String message, LocalDateTime timeStamp, Boolean isSuccess, Integer statusCode) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.isSuccess = isSuccess;
        this.statusCode = statusCode;
    }
}
