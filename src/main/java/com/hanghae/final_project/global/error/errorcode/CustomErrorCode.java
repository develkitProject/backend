package com.hanghae.final_project.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CustomErrorCode implements  Errorcode{
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED,"권한이 없습니다"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST,"잘못된 요청입니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
