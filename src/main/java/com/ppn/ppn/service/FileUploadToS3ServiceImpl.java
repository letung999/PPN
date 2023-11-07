package com.ppn.ppn.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ppn.ppn.service.constract.IFileUploadToS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadToS3ServiceImpl implements IFileUploadToS3Service {
    @Value("${aws.ppn.bucketName}")
    private String bucketName;
    private final AmazonS3 amazonS3;
    @Override
    public List<String> uploadFileToS3(List<MultipartFile> multipartFiles) {
        List<String> urls = multipartFiles.stream().map(multipartFile -> {
            String fileName = generateFileName(multipartFile);
            String url = null;
            try {
                url = upload(multipartFile, fileName, bucketName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return url;
        }).collect(Collectors.toList());
        return urls;
    }

    private String upload(MultipartFile multipartFile, String fileName, String bucketName) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        }
        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        metadata.addUserMetadata("Title", "File Upload - " + fileName);
        metadata.setContentLength(file.length());
        request.setMetadata(metadata);
        amazonS3.putObject(request);
        // delete file
        file.delete();
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}
