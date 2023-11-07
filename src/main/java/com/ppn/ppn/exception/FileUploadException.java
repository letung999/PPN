package com.ppn.ppn.exception;

import lombok.Getter;
import lombok.Setter;

import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_UP_LOAD_FILE;

@Getter
@Setter
public class FileUploadException extends RuntimeException {
    private String fileName;

    public FileUploadException(String fileName) {
        super(String.format("%s: ", ERR_MSG_UP_LOAD_FILE, fileName));
        this.fileName = fileName;
    }
}
