package com.springboot3base.domain.user.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.springboot3base.common.config.QueryDSLConfig;
import com.springboot3base.common.model.response.PageContentResDto;
import com.springboot3base.domain.user.dto.UserDetailResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot3base.domain.user.dto.UserFilterReqDto;
import com.springboot3base.domain.user.dto.UserResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.springboot3base.domain.auth.entity.QRoleEntity.roleEntity;
import static com.springboot3base.domain.user.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
    private final QueryDSLConfig queryDSLConfig;
    private final JPAQueryFactory jpaQueryFactory;

    public Optional<UserDetailResDto> getDetail(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(UserDetailResDto.class,
                        userEntity.id,
                        userEntity.username,
                        userEntity.name,
                        roleEntity.roleName.as("roleCd"),
                        userEntity.phone,
                        userEntity.email,
                        userEntity.createDate
                ))
                .from(userEntity)
                .where(userEntity.id.eq(id))
                .fetchOne());
    }

    public Page<UserResDto> filter(UserFilterReqDto reqDto, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (reqDto.getName() != null) builder.and(userEntity.name.contains(reqDto.getName()));
        if (reqDto.getPhone() != null) builder.and(userEntity.phone.contains(reqDto.getPhone()));
        if (reqDto.getEmail() != null) builder.and(userEntity.email.contains(reqDto.getEmail()));
        if (reqDto.getApproved() != null) builder.and(userEntity.approved.eq(reqDto.getApproved()));
        builder.and(userEntity.del.isFalse());

        List<OrderSpecifier<?>> orders = getOrderSpecifiers(pageable);

        List<UserResDto> content = jpaQueryFactory
                .selectDistinct(Projections.constructor(UserResDto.class,
                        userEntity.id,
                        userEntity.username,
                        userEntity.name,
                        userEntity.role.roleName,
                        userEntity.phone,
                        userEntity.email,
                        userEntity.approved,
                        userEntity.createDate
                ))
                .from(userEntity)
                .where(builder)
                .orderBy(orders.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(jpaQueryFactory
                .select(Wildcard.count)
                .from(userEntity)
                .where(builder)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createDate" -> {
                        OrderSpecifier<?> orderUserId = queryDSLConfig.getSortedColumn(direction, userEntity, "createDate");
                        orders.add(orderUserId);
                    }
                    case "name" -> {
                        OrderSpecifier<?> orderName = queryDSLConfig.getSortedColumn(direction, userEntity, "name");
                        orders.add(orderName);
                    }
                    case "id" -> {
                        OrderSpecifier<?> orderDepartment = queryDSLConfig.getSortedColumn(direction, userEntity, "id");
                        orders.add(orderDepartment);
                    }
                    default -> {
                    }
                }
            }
        }
        return orders;
    }
}
