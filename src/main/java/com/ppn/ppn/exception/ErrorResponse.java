package com.ppn.ppn.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private LocalDateTime localDateTime;
    private String path;
    private String message;
    private String statusCode;
}
