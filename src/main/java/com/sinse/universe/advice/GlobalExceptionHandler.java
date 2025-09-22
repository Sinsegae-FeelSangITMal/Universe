package com.sinse.universe.advice;

import com.sinse.universe.dto.response.ApiResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;


// 컨트롤러 단에서 발생한 예외를 잡아서 처리하는 곳
// 사용할때는 throw new CustomException(ErrorCode.xxx)
// -> 던져진 에러는 이곳에서 처리
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException e) {
        return ApiResponse.error(e.getErrorCode());
    }

    /*
        {
          "success": false,
          "message": "입력값이 올바르지 않습니다.",
          "code": "INVALID_INPUT",
          "data": [
                {
                  "field": "email",
                  "message": "올바른 이메일 형식이 아닙니다."
                },
                {
                  "field": "password",
                  "message": "비밀번호는 8자 이상이어야 합니다."
                }
            ]
        }
 */

    /**
     * @Valid 검증 과정에서 발생한 에러를 잡아서 처리하는 곳
     * @Param e : 어떤 필드에서 에러가 났는지 정보도 보유하고 있어서 그걸 응답메시지에 담아 프론트로 전송.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        List<Map<String, String>> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                                "field", error.getField(),
                                "message", error.getDefaultMessage()
                        )
                )
                .toList();

        return ApiResponse.error(ErrorCode.INVALID_INPUT, errors);
    }
}