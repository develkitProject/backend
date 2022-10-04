package com.hanghae.final_project.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hanghae.final_project.global.dto.ResponseDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        ResponseDto.fail("AUTHORIZE_FAIL", accessDeniedException.getMessage())
                )
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
