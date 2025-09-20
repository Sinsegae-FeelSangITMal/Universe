package com.sinse.universe.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400 BAD_REQUEST
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    //verification = 이메일 인증
    VERIFICATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),


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
