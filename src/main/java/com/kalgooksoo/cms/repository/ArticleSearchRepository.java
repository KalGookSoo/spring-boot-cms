package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.Article;
import com.kalgooksoo.cms.search.ArticleSearch;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ArticleSearchRepository implements SearchRepository<Article, ArticleSearch> {

    private final EntityManager em;

    @Override
    public Page<Article> search(@NonNull ArticleSearch articleSearch) {
        return null;
    }

}
