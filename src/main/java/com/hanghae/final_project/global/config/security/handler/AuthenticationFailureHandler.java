package com.hanghae.final_project.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hanghae.final_project.global.dto.ResponseDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {


        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        ResponseDto.fail("INVALID INFO", "회원정보가 일치하지 않습니다.")
                )
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
