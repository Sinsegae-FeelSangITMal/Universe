package com.sinse.universe.model.user;

import com.sinse.universe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndOauthId(String provider, String oauthId);
}
