package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.repository.ArticleRepository;
import com.kalgooksoo.cms.board.search.ArticleSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultArticleService implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public Page<Article> findAll(@NonNull ArticleSearch search) {
        return articleRepository.searchAll(search);
    }
}
