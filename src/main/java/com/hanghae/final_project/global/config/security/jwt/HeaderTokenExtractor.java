package com.hanghae.final_project.global.config.security.jwt;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class HeaderTokenExtractor {

    /*
     * HEADER_PREFIX
     * header Authorization token 값의 표준이되는 변수
     * 우리가 만든 token 앞에 Bearer
     */

    public final String HEADER_PREFIX="Bearer ";

    public String extract(String header ){

        /*
         * - Token 값이 올바르지 않은경우 -
         * header 값이 비어있거나 또는 HEADER_PREFIX 값보다 짧은 경우
         * 이셉션을(예외)를 던져주어야 합니다.
         */

        if(header == null || header.equals("") || header.length() < HEADER_PREFIX.length()){
            throw new AuthenticationServiceException("올바른 JWT 정보가 아닙니다.");
        }

        return header.substring(HEADER_PREFIX.length());
    }
}
