package com.ppn.ppn.exception;

import lombok.Getter;
import lombok.Setter;

import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_DOWN_LOAD_FILE;

@Getter
@Setter
public class FileDownloadException extends RuntimeException {
    private String fileName;

    public FileDownloadException(String fileName) {
        super(String.format("%s in " + ERR_MSG_DOWN_LOAD_FILE, fileName));
        this.fileName = fileName;
    }
}
