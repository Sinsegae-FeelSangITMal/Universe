package com.sinse.universe.dto.request;

import com.sinse.universe.enums.UserStatus;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter   //spring 파라미터 바인딩을 위한 setter
@ToString
public class UserSearchRequest {
    private String loginId;
    private Integer roleId;  // roleId 조건이 없을때 숫자 0이 아닌 null로 처리하기 위해 Integer 사용
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private UserStatus status;
}
