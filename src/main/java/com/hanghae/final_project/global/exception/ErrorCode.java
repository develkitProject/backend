package com.hanghae.final_project.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED,"권한이 없습니다"),

    USER_DUPLICATED(HttpStatus.CONFLICT, "중복된 아이디입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST,"잘못된 요청입니다."),

    COMMON_BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    COMMON_INTERNAL_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생하였습니다."),

    // JWT 관련
    JWT_BAD_TOKEN_400(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    JWT_UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    JWT_NOT_FOUND_404(HttpStatus.NOT_FOUND, "유효한 JWT 토큰이 없습니다."),
    JWT_NOT_ALLOWED_405(HttpStatus.METHOD_NOT_ALLOWED, "지원되지 않는 JWT 토큰입니다."),

    // USER 관련
    USER_INFO_NOT_FORMATTED(HttpStatus.NOT_ACCEPTABLE, ""),
    USER_LOGINID_NOT_FOUND_404(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    USER_LOGINID_DUPLICATION_409(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    USER_EMAIL_NOT_FORMATTED(HttpStatus.NOT_ACCEPTABLE, "Email 형식이 일치하지 않습니다"),

    //수정 필요
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "User이름을 찾을 수 없습니다"),

    // Document 관련
    DOCUMENT_NOT_FOUND_404(HttpStatus.NOT_FOUND, "요청한 문서 ID가 없습니다."),
    DOCUMENT_DUPLICATION_409(HttpStatus.CONFLICT, "이미 등록된 문서 ID 입니다."),

    // Folder 관련
    FOLDER_NOT_FOUND_404(HttpStatus.NOT_FOUND, "요청한 폴더 ID가 없습니다."),
    FOLDER_DUPLICATION_409(HttpStatus.CONFLICT, "이미 등록된 폴더 ID 입니다."),

    // Notice 관련
    NOTICE_NOT_FOUND_404(HttpStatus.NOT_FOUND, "해당 공지사항이 존재하지 않습니다."),

    // Role 관련
    NO_PERMISSION_TO_WRITE_NOTICE_400(HttpStatus.FORBIDDEN, "공지 사항 작성 권한이 없습니다."),
    NO_PERMISSION_TO_MODIFY_NOTICE_400(HttpStatus.FORBIDDEN, "공지 사항 수정 권한이 없습니다."),
    NO_PERMISSION_TO_DELETE_NOTICE_400(HttpStatus.FORBIDDEN, "공지 사항 삭제 권한이 없습니다."),

    // Workspace 관련
    WORKSPACE_INFO_NOT_FORMATTED(HttpStatus.NOT_ACCEPTABLE, ""),
    WORKSPACE_NOT_FOUND_404(HttpStatus.NOT_FOUND, "해당 WORKSPACE가 존재하지 않습니다"),
    WORKSPACE_IN_USER_NOT_FOUND_404(HttpStatus.NOT_FOUND, "해당 WORKSPACE에 존재하는 사용자가 아닙니다"),
    WORKSPACE_DUPLICATION_409(HttpStatus.CONFLICT, "해당 사용자가 WORKSPACE에 존재합니다"),
    WORKSPACE_INVITATION_CODE_NOT_SAME(HttpStatus.FORBIDDEN, "초대 코드가 동일하지 않습니다"),
    WORKSPACE_INVITATION_CODE_DOES_NOT_EXIST(HttpStatus.FORBIDDEN, "초대 코드를 입력해주세요"),

    // Schedule 관련
    SCHEDULE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Schedule이 존재하지 않습니다"),

    //이미지 관련
    NO_IMAGE_FILE(HttpStatus.BAD_REQUEST,"이미지 파일을 확인해 주세요"),

    //초대코드 관련
    NO_INVITATION_CODE_404(HttpStatus.NOT_FOUND,"해당 코드가 유효하지 않습니다"),

    //검색 관련
    SEARCH_TYPE_BAD_REQUEST(HttpStatus.NOT_FOUND,"검색 type을 확인해 주세요");

    private HttpStatus httpStatus;
    private String message;
}
