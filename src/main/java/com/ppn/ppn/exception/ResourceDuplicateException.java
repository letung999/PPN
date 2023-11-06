package com.ppn.ppn.exception;

import lombok.Getter;
import lombok.Setter;

import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_DATA_DUPLICATED;

@Getter
@Setter
public class ResourceDuplicateException extends RuntimeException {
    private String fieldName;
    private String value;

    public ResourceDuplicateException(String fieldName, String value) {
        super(String.format("%s " + ERR_MSG_DATA_DUPLICATED + " with value: %s", fieldName, value));
        this.fieldName = fieldName;
        this.value = value;
    }
}
