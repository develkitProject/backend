package com.hanghae.final_project.global.config.security.provider;

import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

public class JwtAuthenticationProvider implements AuthenticationProvider{


    @Resource(name="userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    public JwtAuthenticationProvider(BCryptPasswordEncoder passwordEncoder){

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        //FormLogin filter 로 부터 생성된 토큰으로부터 아이디와 비밀번호 조회함

        String username = token.getName();
        String password = (String) token.getCredentials();


        UserDetailsImpl userDetails =(UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        //비번일치 않을 경우, exception -> FormLoginFailureHandler 로 이동
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw  new BadCredentialsException("INVALID USER INFO");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
