package com.ppn.ppn.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExceptionResponse {
    List<ErrorParameter> errors;
}
