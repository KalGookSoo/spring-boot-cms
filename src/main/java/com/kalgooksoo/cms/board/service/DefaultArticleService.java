package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.repository.ArticleRepository;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
import com.kalgooksoo.cms.board.search.ArticleSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DefaultArticleService implements ArticleService {

    private final CategoryRepository categoryRepository;

    private final ArticleRepository articleRepository;

    @Override
    public Page<Article> findAll(@NonNull ArticleSearch search) {
        return articleRepository.searchAll(search);
    }

    @Override
    public Article create(@NonNull CreateArticleCommand command) {
        Category category = categoryRepository.getReferenceById(command.categoryId());
        Article article = Article.create(command.title(), command.content());
        category.addArticle(article);
        categoryRepository.save(category);
        return article;
    }

    @Override
    public Article find(@NonNull String id) {
        return articleRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
