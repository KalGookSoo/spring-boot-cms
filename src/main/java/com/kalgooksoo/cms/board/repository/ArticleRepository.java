package com.kalgooksoo.cms.board.repository;

import com.kalgooksoo.cms.board.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
