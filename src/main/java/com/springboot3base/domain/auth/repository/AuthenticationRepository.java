package com.springboot3base.domain.auth.repository;

import com.springboot3base.domain.auth.entity.AuthenticationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<AuthenticationEntity, String> {
    Optional<AuthenticationEntity> findById(String s);
    Optional<AuthenticationEntity> findByRefreshToken(String token);
    Optional<AuthenticationEntity> findFirstByUserIdAndHasRevokedOrderByCreateDateDesc(Long id, Boolean logout);
    Optional<AuthenticationEntity> findByUserId(Long userId);
}
