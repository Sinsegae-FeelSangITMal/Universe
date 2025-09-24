package com.sinse.universe.exception;

import com.sinse.universe.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDetail(), cause);
        this.errorCode = errorCode;
    }
}

// 개발자가 동적으로 추가 정보를 전달하고 싶은 경우 message로 전달
// exception의 message는 서버 로그로 찍히고, ErrorCode의 detail은 프론트에서 사용