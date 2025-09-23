package com.sinse.universe.model.partner;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, Integer> {

}
