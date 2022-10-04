package com.hanghae.final_project.domain.model;

public enum UserSocialEnum {

    STANDARD(Social.STANDARD),
    KAKAO(Social.KAKAO)

    ;
    private final String social;
    UserSocialEnum(String social){this.social=social;}
    public static class Social{
        public static final String STANDARD="SOCIAL_STANDARD";
        public static final String KAKAO = "SOCIAL_KAKAO";
    }
}
