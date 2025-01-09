package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.User;
import com.kalgooksoo.cms.search.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface UserSearchRepository {
    Page<User> searchAll(@NonNull UserSearch search, @NonNull Pageable pageable);
}
