package com.hanghae.final_project.global.error.exception;


import com.hanghae.final_project.global.error.errorcode.Errorcode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends  RuntimeException {
    private final Errorcode errorcode;
    private String message;


    public RestApiException(Errorcode errorcode, String message){
        this.errorcode=errorcode;
        this.message=message;
    }
}
