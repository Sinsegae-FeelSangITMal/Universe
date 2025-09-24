package com.sinse.universe.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400 BAD_REQUEST
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    PASSWORD_CONFIRM_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "잘못된 권한명입니다."),
    DATE_INVALID_RANGE(HttpStatus.BAD_REQUEST, "종료일은 시작일보다 나중이어야 합니다."),
    DATE_START_IN_FUTURE(HttpStatus.BAD_REQUEST, "시작읠은 현재 날짜보다 미래일 수 없습니다."),
    INVALID_SORT_PROPERTY(HttpStatus.BAD_REQUEST,"허용되지 않은 정렬 컬럼입니다."),


    //verification = 이메일 인증
    VERIFICATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),
    NOT_VERIFIED_EMAIL(HttpStatus.BAD_REQUEST, "인증되지 않은 이메일입니다. 인증을 시도해주세요"),


    // 500 INTERNAL_SERVER_ERROR
    MAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다."),
    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled Exception 발생");


    private final HttpStatus httpStatus;
    private final String detail;

    ErrorCode(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    public String getDetail(){
        return detail;
    }
}
