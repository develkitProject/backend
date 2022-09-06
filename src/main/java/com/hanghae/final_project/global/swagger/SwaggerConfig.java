package com.hanghae.final_project.global.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    //스웨거 페이지에 소개될 설명들
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("cheoljun")
                .description("Image EXAMPLE")
                .build();
    }

    @Bean
    public Docket commonApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .groupName("swg-group1")//빈설정을 여러개 해줄경우 구분하기 위한 구분자.
                .useDefaultResponseMessages(false)
                .select()//apis, paths를 사용해주기 위한 builder
                .apis(RequestHandlerSelectors.basePackage("com.hanghae.final_project"))
                .paths(PathSelectors.any())//스웨거에서 보여줄 api 필터링
                .build()
                .apiInfo(apiInfo())//스웨거 설명
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));

    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
