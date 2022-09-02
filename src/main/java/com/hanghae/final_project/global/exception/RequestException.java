package com.hanghae.final_project.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RequestException extends RuntimeException{

    private String errorCode;
    private final HttpStatus httpStatus;
    private String message;

    public RequestException(ErrorCode errorcode) {
        super(errorcode.getMessage());
        this.errorCode = errorcode.toString();
        this.message = errorcode.getMessage();
        this.httpStatus = errorcode.getHttpStatus();
    }

    public RequestException(ErrorCode errorcode, String message) {
        super(message);
        this.errorCode = errorcode.toString();
        this.message = message;
        this.httpStatus = errorcode.getHttpStatus();
    }

}
