package com.hanghae.final_project.global.config.security;



import com.hanghae.final_project.global.config.security.filter.JwtAuthenticationFilter;
import com.hanghae.final_project.global.config.security.filter.JwtAuthorizationFilter;
import com.hanghae.final_project.global.config.security.handler.AccessDeniedHandler;
import com.hanghae.final_project.global.config.security.handler.AuthenticationFailureHandler;
import com.hanghae.final_project.global.config.security.handler.AuthenticationSuccessHandler;
import com.hanghae.final_project.global.config.security.handler.AuthorizationFailureHandler;
import com.hanghae.final_project.global.config.security.jwt.HeaderTokenExtractor;
import com.hanghae.final_project.global.config.security.provider.JwtAuthenticationProvider;
import com.hanghae.final_project.global.config.security.provider.JwtAuthorizationProvider;
import com.hanghae.final_project.global.util.annotation.QueryStringArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {

    private final JwtAuthorizationProvider jwtAuthorizationProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final HeaderTokenExtractor headerTokenExtractor;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final AccessDeniedHandler accessDeniedHandler;

    private final AuthorizationFailureHandler authorizationFailureHandler;



    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .antMatchers(
                        "/swagger-ui/**",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/swagger/**",
                        "/h2-console/**",
                        "/stomp/chat/**",
                        "/actuator/**"
        );

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManagerBuilder auth) throws Exception {

        //인증 (Authentication)**: 사용자 신원을 확인하는 행위
        //인가 (Authorization)**: 사용자 권한을 확인하는 행위
        auth
                .authenticationProvider(jwtAuthorizationProvider)
                .authenticationProvider(jwtAuthenticationProvider());

        http.csrf().disable();
        http.cors().configurationSource(corsConfigurationSource());



        http
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //사용하는 필터 만들기
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration));
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/members/login");

        jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        jwtAuthenticationFilter.afterPropertiesSet();

        return jwtAuthenticationFilter;
    }

    //사용하는 필터 만들기
    private JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {

        List<String> skipPathList = new ArrayList<>();

        // 회원 관리 API SKIP 적용
        skipPathList.add("POST,/api/members/signup");
        skipPathList.add("POST,/api/members/email");
        skipPathList.add("GET,/user/kakao/callback/**");
        skipPathList.add("GET,/api/members/guest");


        //WebSocket 관련 -> Filter 역할 Intercepter로 대신함.
        skipPathList.add("GET,/stomp/chat/**");
        skipPathList.add("GET,/chat/**");


        //기본 페이지 설정
        skipPathList.add("GET,/");
        skipPathList.add("GET,/favicon.ico");
        skipPathList.add("GET,/error");

        FilterSkipMatcher matcher = new FilterSkipMatcher(skipPathList, "/**");
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter(headerTokenExtractor, matcher);

        filter.setAuthenticationFailureHandler(authorizationFailureHandler);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));

        return filter;
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(encodePassword());
    }


    //cors 허용 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://d-velkit.com");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
