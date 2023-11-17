package com.ppn.ppn.exception;

import lombok.Getter;
import lombok.Setter;

import static com.ppn.ppn.constant.MessageStatus.ERR_MSG_CAN_NOT_DELETE_CACHE;

@Getter
@Setter
public class CacheNotDeletedException extends RuntimeException {
    private String prefix;

    public CacheNotDeletedException(String prefix) {
        super(String.format(ERR_MSG_CAN_NOT_DELETE_CACHE + prefix));
        this.prefix = prefix;
    }
}
