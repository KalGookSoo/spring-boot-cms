package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, String> {

    @NonNull
    @EntityGraph(attributePaths = {"category", "replies", "votes", "views", "attachments"})
    Optional<Article> findById(@NonNull String id);

}
