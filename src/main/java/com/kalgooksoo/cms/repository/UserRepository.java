package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * 계정 저장소
 */
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(@NonNull String username);
}
