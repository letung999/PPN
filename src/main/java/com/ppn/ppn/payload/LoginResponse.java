package com.ppn.ppn.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class LoginResponse<T> {
    private HttpStatus status;
    private T data;

    private String message;

    public LoginResponse(HttpStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public LoginResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }


    public LoginResponse(HttpStatus status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

}
