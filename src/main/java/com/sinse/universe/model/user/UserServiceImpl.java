package com.sinse.universe.model.user;

import com.sinse.universe.domain.User;
import com.sinse.universe.dto.request.UserJoinRequest;
import com.sinse.universe.dto.request.UserSearchRequest;
import com.sinse.universe.dto.response.UserListForAdminResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.enums.UserStatus;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.role.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl {

    private final RoleServiceImpl roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(RoleServiceImpl roleService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void checkDuplicateEmail(String loginId){
        if(userRepository.existsByLoginId(loginId)){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    public User createUser(UserJoinRequest form) {
        User user = User.builder()
                .loginId(form.email())
                .name(form.name())
                .password(passwordEncoder.encode(form.password())) // 암호화
                .role(roleService.findByName(form.role()))
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    // 조회
    public Page<UserListForAdminResponse> getUesrListForAdmin(UserSearchRequest dto, Pageable pageable) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        log.debug("오늘 날짜 {}", today);

        // 날짜 유효성 검사
        if( startDate != null && startDate.isAfter(today)) {
            throw new CustomException(ErrorCode.DATE_START_IN_FUTURE);
        }
        if(endDate != null && endDate.isBefore(startDate)){
            throw new CustomException(ErrorCode.DATE_INVALID_RANGE);
        }

        // DTO는 LocalDate 타입 -> repository 계층에 전달하기 전 LocalDateTime 타입으로 변환
        LocalDateTime startDateTime = startDate == null ? null : startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate == null ? null : endDate.atTime(LocalTime.MAX);

        Page<User> userPage = userRepository.searchUsers(dto, startDateTime, endDateTime, pageable);
        Page<UserListForAdminResponse> dtoPage = userPage.map(UserListForAdminResponse::from); //spring data jpa가 Page<T> map() 메서드 제공.다시 Page<T> 반환
        return dtoPage;
    }
}
