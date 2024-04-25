package com.kalgooksoo.cms.security.repository;

import com.kalgooksoo.cms.security.entity.RememberMeToken;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RememberMeTokenRepository extends Repository<RememberMeToken, String> {
    void save(RememberMeToken rememberMeToken);
    Optional<RememberMeToken> findBySeries(String series);
    void deleteByUsername(String username);
}
