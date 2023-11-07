package com.ppn.ppn.service.constract;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileUploadToS3Service {
    List<String> uploadFileToS3(List<MultipartFile> multipartFiles);
}
