package com.sinse.universe.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400 BAD_REQUEST
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    PASSWORD_CONFIRM_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "잘못된 권한명입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."),
    ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입한 회원입니다."),
    //verification = 이메일 인증
    VERIFICATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),
    NOT_VERIFIED_EMAIL(HttpStatus.BAD_REQUEST, "인증되지 않은 이메일입니다. 인증을 시도해주세요"),

    // 401
    INVALID_REFRESHTOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 refresh token입니다."),


    // 소속사 Partner
    PARTNER_NOT_FOUND(HttpStatus.NOT_FOUND, "파트너를 찾을 수 없습니다."),

    // 아티스트 Artist
    ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND, "아티스트를 찾을 수 없습니다."),
    ARTIST_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 아티스트 이름입니다."),
    ARTIST_DELETE_NOT_ALLOWED(HttpStatus.CONFLICT, "연관 데이터가 있어 아티스트를 삭제할 수 없습니다."),

    // 멤버 Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),
    MEMBER_ARTIST_REQUIRED(HttpStatus.BAD_REQUEST, "소속 아티스트는 필수 값입니다."),

    // 아티스트 커스텀 컬러 Artist_bg_color
    COLOR_NOT_FOUND(HttpStatus.NOT_FOUND, "컬러를 찾을 수 없습니다."),

    // 라이브 Stream
    STREAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스트림을 찾을 수 없습니다."),

    // 라이브 상품 Stream Product
    STREAM_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 스트림 상품을 찾을 수 없습니다."),
    FILE_DELETE_FAILED(HttpStatus.BAD_REQUEST,"파일을 삭제할 수 없습니다."),

    // 프로모션 상품 Promotion
    PROMOTION_NOT_FOUND(HttpStatus.NOT_FOUND,"프로모션을 찾을 수 없습니다."),

    PRODUCT_REQUIRED(HttpStatus.BAD_REQUEST, "상품을 선택해야 합니다"),
    PROMOTION_REQUIRED(HttpStatus.BAD_REQUEST, "프로모션 상품을 선택해야 합니다"),


    // 404 NOT_FOUND
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"),

    // 413 PAYLOAD_TOO_LARGE
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "업로드 가능한 파일 크기를 초과했습니다."),

    // 장바구니 Cart
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에 해당 항목이 없습니다."),
    CART_LIMIT(HttpStatus.CONFLICT, "유저당 구매 제한을 초과했습니다."),
    CART_NO_STOCK(HttpStatus.CONFLICT, "상품 "),

    //--------------------------------------------------------------------------------------
    //      500 INTERNAL_SERVER_ERROR
    //--------------------------------------------------------------------------------------
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
