package com.hanghae.final_project.global.error.errorcode;


import org.springframework.http.HttpStatus;

public interface Errorcode {

    String name();
    HttpStatus getHttpStatus();
    String getMessage();
}
