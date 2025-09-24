package com.sinse.universe.exception;

import com.sinse.universe.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDetail());  //에러코드 메시지를 예외메시지로 전달
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDetail(), cause);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(errorCode.getDetail() + " [" + message + "]");
        this.errorCode = errorCode;
    }
}
