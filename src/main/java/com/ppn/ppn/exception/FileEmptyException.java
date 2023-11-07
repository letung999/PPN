package com.ppn.ppn.exception;

import lombok.Getter;
import lombok.Setter;

import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_EMPTY_FILE;

@Getter
@Setter
public class FileEmptyException extends RuntimeException {
    private String fileName;

    public FileEmptyException(String fileName) {
        super(String.format("%s with: " + ERR_MSG_EMPTY_FILE, fileName));
        this.fileName = fileName;
    }
}
