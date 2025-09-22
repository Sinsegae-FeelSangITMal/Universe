package com.sinse.universe.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sinse.universe.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * Http 응답 메시지 구조 표준화
 */

@Getter
@Builder
@JsonPropertyOrder({"success", "message", "code", "data"})   // 항상 지정한 순서대로 JSON 직렬화
public class ApiResponse<T> {

    private boolean success;     // (에러, 비즈니스적인 실패 모두 false)
    private String message;      // 사용자에게 보여줄 메시지 ex) "이메일 형식이 올바르지 않습니다."
    private String code;         // 메시지를 간결하게 표현한 코드 ex) "SUCCESS", "INVALID_EMAIL_FORMAT"
    private T data;              // 보낼 데이터가 있으면 담기

    public ApiResponse(boolean success, String message, String code,  T data){
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    // 성공응답 - 함께 보낼 데이터 존재
    // 사용: ApiResponse.success(String msg, Object data)
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .code("SUCCESS")
                .data(data)
                .build()
        );
    }

    // 성공응답 - 데이터 없음    // 사용: ApiResponse.success(String msg)
    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        return success(message, null);
    }

    // 실패응답
    // 사용: ApiResponse.error(ErrorCode ec, Object data)
    public static ResponseEntity<ApiResponse<Object>> error(ErrorCode errorCode, Object data){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(errorCode.getDetail())
                        .code(errorCode.name())
                        .data(data)
                        .build()
                );
    }

    // 사용: ApiResponse.error(ErrorCode ec)
    public static ResponseEntity<ApiResponse<Object>> error(ErrorCode errorCode){
        return error(errorCode, null);
    }
}