package com.kalgooksoo.cms.repository;

import com.kalgooksoo.core.page.PageVO;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

public interface SearchRepository<T, Q extends PageVO> {
    Page<T> search(@NonNull Q q);
}
