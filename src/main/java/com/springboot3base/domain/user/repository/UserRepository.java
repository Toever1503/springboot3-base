package com.springboot3base.domain.user.repository;

import com.springboot3base.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameAndDel(String username, Boolean del);
    Optional<UserEntity> findByUsernameAndNameAndEmail(String username, String name, String email);
}
