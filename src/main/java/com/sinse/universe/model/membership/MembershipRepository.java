package com.sinse.universe.model.membership;

import com.sinse.universe.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    Optional<Membership> findTopByUserIdAndArtistIdOrderByStartDateDesc(int userId, int artistId);
}