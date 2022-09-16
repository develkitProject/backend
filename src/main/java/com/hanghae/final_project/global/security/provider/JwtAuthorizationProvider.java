package com.hanghae.final_project.global.security.provider;



import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.repository.UserRepository;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import com.hanghae.final_project.global.security.jwt.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component

public class JwtAuthorizationProvider implements AuthenticationProvider {
    private final JwtDecoder jwtDecoder;

    private final UserRepository userRepository;

    @Autowired
    public JwtAuthorizationProvider(JwtDecoder jwtDecoder, UserRepository userRepository){
        this.jwtDecoder=jwtDecoder;
        this.userRepository=userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = (String) authentication.getPrincipal();
        String username = jwtDecoder.decodeUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new AuthenticationCredentialsNotFoundException("해당 회원정보가 없습니다."));
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}