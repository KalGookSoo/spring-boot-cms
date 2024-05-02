package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.search.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

public interface ArticleService {

    Page<Article> findAll(@NonNull ArticleSearch search);

    Article create(@NonNull CreateArticleCommand command);

    Article find(@NonNull String id);
}
