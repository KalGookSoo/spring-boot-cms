package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.search.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

public interface ArticleService {

    Page<Article> findAll(@NonNull ArticleSearch search);
}
