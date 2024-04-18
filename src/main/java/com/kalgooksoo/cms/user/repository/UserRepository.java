package com.kalgooksoo.cms.user.repository;

import com.kalgooksoo.cms.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, String> {
    User save(User user);

    Optional<User> findById(String id);

    Page<User> findAll(Pageable pageable);

    void deleteById(String id);

    Optional<User> findByUsername(String username);

    Page<User> searchAll(String keyword, Pageable pageable);

}
