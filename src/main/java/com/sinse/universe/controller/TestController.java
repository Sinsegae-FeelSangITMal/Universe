package com.sinse.universe.controller;


import com.sinse.universe.dto.request.UserSearchRequest;
import com.sinse.universe.dto.response.UserListForAdminResponse;
import com.sinse.universe.model.user.UserRepository;
import com.sinse.universe.model.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/test/admin/users")
    public Page<UserListForAdminResponse> getUsers(
            UserSearchRequest userSearchRequest,
            // 기본 정렬 user_id(pk) 최신순
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return userServiceImpl.getUesrListForAdmin(userSearchRequest, pageable);
    }
}
