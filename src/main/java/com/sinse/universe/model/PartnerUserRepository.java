package com.sinse.universe.model;

import com.sinse.universe.domain.PartnerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerUserRepository extends JpaRepository<PartnerUser, Integer> {
    Optional<PartnerUser> findByUserId(int userId);
}
