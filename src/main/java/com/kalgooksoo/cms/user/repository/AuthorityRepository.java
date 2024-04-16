package com.kalgooksoo.cms.user.repository;

import com.kalgooksoo.cms.user.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    void deleteByUserId(String userId);
    List<Authority> findByUserId(String userId);
}
