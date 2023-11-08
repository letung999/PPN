package com.ppn.ppn.controller;

import com.ppn.ppn.exception.FileEmptyException;
import com.ppn.ppn.payload.APIResponse;
import com.ppn.ppn.service.FileUploadToS3ServiceImpl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ppn.ppn.constant.MessageStatus.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/file")
public class FileController {
    @Autowired
    private FileUploadToS3ServiceImpl fileUploadToS3Service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("file") List<MultipartFile> multipartFile) throws FileEmptyException, IOException {
        if (multipartFile.size() == 0) {
            throw new FileEmptyException(ERR_MSG_EMPTY_FILE);
        }
        List<String> allowedFileExtensions = new ArrayList<>(Arrays.asList("pdf", "txt", "epub", "csv", "png", "jpg", "jpeg", "srt"));
        Set<String> inputFileExtension = multipartFile.stream().map(extension -> {
            return FilenameUtils.getExtension(extension.getOriginalFilename());
        }).collect(Collectors.toSet());

        if (allowedFileExtensions.containsAll(inputFileExtension)) {
            List<String> urls = fileUploadToS3Service.uploadFileToS3(multipartFile);
            APIResponse apiResponse = APIResponse.builder()
                    .message(INF_MSG_SUCCESSFULLY)
                    .timeStamp(LocalDateTime.now())
                    .isSuccess(true)
                    .data(urls)
                    .statusCode(200)
                    .build();
            return ResponseEntity.ok(apiResponse);
        }
        APIResponse apiResponse = APIResponse.builder()
                .message(ERR_MSG_UP_LOAD_FILE)
                .timeStamp(LocalDateTime.now())
                .isSuccess(true)
                .data(null)
                .statusCode(400)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFileFromS3(@RequestParam("fileName") @NotBlank @NotNull String fileName) throws IOException {
        Object response = fileUploadToS3Service.downloadFileFromS3(fileName);
        if (response != null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(response);
        } else {
            APIResponse apiResponse = APIResponse.builder()
                    .message("File could not be downloaded")
                    .isSuccess(false)
                    .statusCode(400)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }
}
