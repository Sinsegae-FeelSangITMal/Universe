package com.sinse.universe.model.user;

import com.sinse.universe.domain.User;
import com.sinse.universe.dto.request.UserSearchRequest;
import com.sinse.universe.dto.response.UserListForAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomUserRepository {
    Page<User> searchUsers(UserSearchRequest request, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
}
