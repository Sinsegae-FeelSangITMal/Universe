package com.sinse.universe.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // ─────────────────────────────────────────────────────────────────────────
    // 400 BAD_REQUEST
    // ─────────────────────────────────────────────────────────────────────────
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),  // 공통 추가
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),   // 공통 추가
    PASSWORD_CONFIRM_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    PRODUCT_REQUIRED(HttpStatus.BAD_REQUEST, "상품을 선택해야 합니다"),
    PROMOTION_REQUIRED(HttpStatus.BAD_REQUEST, "프로모션 상품을 선택해야 합니다"),
    MEMBER_ARTIST_REQUIRED(HttpStatus.BAD_REQUEST, "소속 아티스트는 필수 값입니다."),
    FILE_DELETE_FAILED(HttpStatus.BAD_REQUEST,"파일을 삭제할 수 없습니다."),
    VERIFICATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),
    NOT_VERIFIED_EMAIL(HttpStatus.BAD_REQUEST, "인증되지 않은 이메일입니다. 인증을 시도해주세요"),
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "업로드 가능한 파일 크기를 초과했습니다."),
    ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "잘못된 권한명입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 요청 파라미터입니다."),


    // ─────────────────────────────────────────────────────────────────────────
    // 401 UNAUTHORIZED
    // ─────────────────────────────────────────────────────────────────────────
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),               // 공통 추가
    INVALID_REFRESHTOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 refresh token입니다."),

    // ─────────────────────────────────────────────────────────────────────────
    // 403 FORBIDDEN
    // ─────────────────────────────────────────────────────────────────────────
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),                   // 공통 추가

    // ─────────────────────────────────────────────────────────────────────────
    // 404 NOT_FOUND
    // ─────────────────────────────────────────────────────────────────────────
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."), // 공통 추가
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    PARTNER_NOT_FOUND(HttpStatus.NOT_FOUND, "파트너를 찾을 수 없습니다."),
    ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND, "아티스트를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),
    COLOR_NOT_FOUND(HttpStatus.NOT_FOUND, "컬러를 찾을 수 없습니다."),
    STREAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스트림을 찾을 수 없습니다."),
    STREAM_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 스트림 상품을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에 해당 항목이 없습니다."),
    PROMOTION_NOT_FOUND(HttpStatus.NOT_FOUND,"프로모션을 찾을 수 없습니다."),

    // ─────────────────────────────────────────────────────────────────────────
    // 409 CONFLICT
    // ─────────────────────────────────────────────────────────────────────────
    CONFLICT(HttpStatus.CONFLICT, "요청이 현재 리소스 상태와 충돌합니다."),     // 공통 추가
    ARTIST_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 아티스트 이름입니다."),
    ARTIST_DELETE_NOT_ALLOWED(HttpStatus.CONFLICT, "연관 데이터가 있어 아티스트를 삭제할 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    ALREADY_REGISTERED_USER(HttpStatus.CONFLICT, "이미 가입한 회원입니다."),
    OUT_OF_STOCK(HttpStatus.CONFLICT, "재고가 부족합니다."),
    CART_LIMIT(HttpStatus.CONFLICT, "유저당 구매 제한을 초과했습니다."),
    CART_NO_STOCK(HttpStatus.CONFLICT, "상품 재고가 없습니다."),

    // ─────────────────────────────────────────────────────────────────────────
    // 500 INTERNAL_SERVER_ERROR
    // ─────────────────────────────────────────────────────────────────────────
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."), // 공통 추가
    STORAGE_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "스토리지 클라이언트 초기화에 실패했습니다."), // 공통 추가
    STORAGE_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "스토리지 서비스 처리중 오류가 발생했습니다."), // 공통 추가
    MAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다."),
    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled Exception 발생");

    private final HttpStatus httpStatus;
    private final String detail;

    ErrorCode(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }
}
