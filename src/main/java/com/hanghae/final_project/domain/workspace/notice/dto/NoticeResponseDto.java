package com.hanghae.final_project.domain.workspace.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NoticeResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    public static <T> NoticeResponseDto<T> success(T data){
        return new NoticeResponseDto<>(true, data, null);
    }
    public static <T> NoticeResponseDto<T>fail(String code, String message){
        return new NoticeResponseDto<>(false, null, new Error(code, message));
    }
    @Getter
    @AllArgsConstructor
    static class Error{
        private String code;
        private String message;
    }


}
