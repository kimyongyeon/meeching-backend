package com.msa.study.meeching.common.security.repository;

import com.msa.study.meeching.common.security.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUid(String email);
}
