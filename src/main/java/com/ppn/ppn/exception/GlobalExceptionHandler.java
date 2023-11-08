package com.ppn.ppn.exception;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode statusCode,
                                                                  WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e -> {
            String fieldName = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            errors.put(fieldName, message);
        });
        ErrorInvalidArgumentResponse errorInvalidArgumentResponse = ErrorInvalidArgumentResponse.builder()
                .path(webRequest.getDescription(false))
                .status(HttpStatusCode.valueOf(400).toString())
                .localDateTime(LocalDateTime.now())
                .error(errors)
                .build();
        return new ResponseEntity<>(errorInvalidArgumentResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleResourcesDuplicateException(ResourceDuplicateException ex,
                                                                           WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .path(webRequest.getDescription(false))
                .message(ex.getMessage())
                .statusCode(HttpStatusCode.valueOf(400).toString())
                .build();

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleInputException.class)
    public ResponseEntity<ErrorResponse> handleRoleInputException(RoleInputException ex,
                                                                  WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .statusCode(HttpStatusCode.valueOf(400).toString())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourcesNotFoundException(ResourcesNotFoundException ex,
                                                                          WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .statusCode(HttpStatusCode.valueOf(404).toString())
                .path(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileDownloadException.class)
    public ResponseEntity<ErrorResponse> handleFileDownloadException(FileDownloadException ex,
                                                                     WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .statusCode(HttpStatusCode.valueOf(204).toString())
                .path(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException ex,
                                                                   WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .statusCode(HttpStatusCode.valueOf(400).toString())
                .path(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileEmptyException.class)
    public ResponseEntity<ErrorResponse> handleFileEmptyException(FileEmptyException ex,
                                                                  WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .statusCode(HttpStatusCode.valueOf(204).toString())
                .path(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NO_CONTENT);
    }

    // handle exception occur when the call was transmitted successfully but Amazon S3 can't process request.
    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<Object> handleAmazonServiceException(AmazonServiceException ex, WebRequest webRequest){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .statusCode(HttpStatusCode.valueOf(409).toString())
                .path(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    //handle exception occur when can't contact with Amazon S3
    @ExceptionHandler(SdkClientException.class)
    public ResponseEntity<Object> handleSdkClientException(SdkClientException ex, WebRequest webRequest){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .statusCode(HttpStatusCode.valueOf(503).toString())
                .path(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
