package com.ppn.ppn.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.ppn.ppn.exception.FileDownloadException;
import com.ppn.ppn.service.constract.IFileUploadToS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Override
    public Object downloadFileFromS3(String fileName) throws IOException {
        if (bucketIsEmpty()) {
            throw new FileDownloadException(fileName);
        }
        S3Object object = amazonS3.getObject(bucketName, fileName);
        try (S3ObjectInputStream s3ObjectInputStream = object.getObjectContent()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                byte[] read_buf = new byte[1024];
                int read_len = 0;
                while ((read_len = s3ObjectInputStream.read(read_buf)) > 0) {
                    fileOutputStream.write(read_buf, 0, read_len);
                }
            }
            Path path = Paths.get(fileName);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileDownloadException(fileName);
            }
        }
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

    //private methods

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private boolean bucketIsEmpty() {
        ListObjectsV2Result objectsV2Result = amazonS3.listObjectsV2(this.bucketName);
        if (objectsV2Result == null) {
            return false;
        }
        List<S3ObjectSummary> objects = objectsV2Result.getObjectSummaries();
        return objects.isEmpty();
    }
}
