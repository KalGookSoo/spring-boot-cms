package com.kalgooksoo.cms.user.repository;

import com.kalgooksoo.cms.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends Repository<User, String> {
    User save(@NonNull User user);

    Optional<User> findById(@NonNull String id);

    Page<User> findAll(@NonNull Pageable pageable);

    void deleteById(@NonNull String id);

    Optional<User> findByUsername(@NonNull String username);
}
