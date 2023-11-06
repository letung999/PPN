package com.ppn.ppn.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
public class ErrorInvalidArgumentResponse {
    private LocalDateTime localDateTime;
    private String path;
    private String status;
    private Map<String, String> error;
}
