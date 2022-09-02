package com.hanghae.final_project.global.exception;

import com.hanghae.final_project.global.error.errorcode.Errorcode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RequestException extends RuntimeException{

    private final HttpStatus httpStatus;

    public RequestException(ErrorCode errorcode) {
        super(errorcode.getMessage());
        this.httpStatus = errorcode.getHttpStatus();
    }

}
