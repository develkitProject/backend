package com.hanghae.final_project.global.config.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtPreProcessingToken extends UsernamePasswordAuthenticationToken {

    private JwtPreProcessingToken(Object principal, Object credentials){
        super(principal, credentials);
    }

    public JwtPreProcessingToken(String token){
       this(
               token,
               token.length()
       );
    }
}
