package com.sinse.universe.model.role;

import com.sinse.universe.domain.Role;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.enums.UserRole;
import com.sinse.universe.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(UserRole name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.ROLE_NOT_FOUND));
    }
}
