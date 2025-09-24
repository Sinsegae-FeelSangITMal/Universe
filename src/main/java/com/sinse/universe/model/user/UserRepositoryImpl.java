package com.sinse.universe.model.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinse.universe.domain.User;
import com.sinse.universe.dto.request.UserSearchRequest;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.sinse.universe.domain.QUser.user;

/**
 * User 검색 동적 쿼리
 * 다중 정렬 X
 * 권한, 상태 등 다중 선택 X
 */

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements CustomUserRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<User> searchUsers(UserSearchRequest request, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        //정렬 관련
        Sort.Order sortOrder = pageable.getSort().iterator().next();  //pageable안에 있는 여러 정렬 조건 중 하나를 꺼낸다.

        String property = sortOrder.getProperty();
        Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

        OrderSpecifier<?> orderSpecifier = null;

        switch (property) {
            case "id" -> orderSpecifier = new OrderSpecifier<>(direction, user.id);
            case "loginId" -> orderSpecifier = new OrderSpecifier<>(direction, user.loginId);
            default -> throw new CustomException(ErrorCode.INVALID_SORT_PROPERTY, "허용되지 않은 컬럼 property=" + property);
        }

        // 1단계: 조건 추가
        if (request.getLoginId() != null) {
            builder.and(user.loginId.contains(request.getLoginId()));  // loginId Like %홍길동%
        }
        if (request.getName() != null) {
            builder.and(user.name.contains(request.getName()));
        }
        if (request.getRoleId() != null) {
            builder.and(user.role.id.eq(request.getRoleId()));   // roleId = 3
        }
        if (request.getStatus() != null) {
            builder.and(user.status.eq(request.getStatus()));
        }
        if (startDateTime != null) {
            builder.and(user.joinDate.goe(startDateTime));  //greater or equal
        }
        if (endDateTime != null) {
            builder.and(user.joinDate.loe(endDateTime));
        }

        // 2단계: 쿼리 실행
        List<User> content = queryFactory
                .selectFrom(user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        // 3단계: 전체 데이터 수 조회
        long total = Optional.ofNullable(
                queryFactory
                    .select(user.count())
                    .from(user)
                    .where(builder)
                    .fetchOne()
            ).orElse(0L);

        // 4단계: Page 객체 반환
        return new PageImpl<>(content, pageable, total);
    }
}
