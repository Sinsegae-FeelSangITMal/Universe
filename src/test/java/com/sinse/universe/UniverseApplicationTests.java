package com.sinse.universe;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinse.universe.domain.QRole;
import com.sinse.universe.domain.Role;
import com.sinse.universe.enums.UserRole;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UniverseApplicationTests {

    @Autowired
    EntityManager em;

    @Test
    void contextLoads() {
        Role role = new Role();
        role.setName(UserRole.TEST);
        em.persist(role);

        JPAQueryFactory query = new JPAQueryFactory(em);
        QRole qRole = QRole.role;

        Role result = query
                .selectFrom(qRole)
                .where(qRole.id.eq(23))
                .fetchOne();

        Assertions.assertThat(result).isEqualTo(role);
        Assertions.assertThat(result.getId()).isEqualTo(role.getId());
    }

}
