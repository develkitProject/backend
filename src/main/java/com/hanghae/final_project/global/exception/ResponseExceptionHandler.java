package com.hanghae.final_project.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Object> handleCustomException(RequestException e) {
        log.info("Exception : '{}'", e.getMessage());
        ErrorResponseDto responseDto = ErrorResponseDto.createDto(e.getErrorCode(), e.getMessage(), e.getHttpStatus());
        return new ResponseEntity<>(responseDto, e.getHttpStatus());
    }
}

@Getter
@Builder
@AllArgsConstructor
class ErrorResponseDto{
    private String errorCode;
    private String message;
    private HttpStatus httpStatus;

    public static ErrorResponseDto createDto(String errorCode, String message, HttpStatus httpStatus){
        return ErrorResponseDto.builder()
                .errorCode(errorCode)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}