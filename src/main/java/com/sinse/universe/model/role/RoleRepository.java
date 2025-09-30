package com.sinse.universe.model.role;


import com.sinse.universe.domain.Role;
import com.sinse.universe.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(UserRole name);
}
