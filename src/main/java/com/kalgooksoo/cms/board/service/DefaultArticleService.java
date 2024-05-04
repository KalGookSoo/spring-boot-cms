package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.command.UpdateArticleCommand;
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

    @Override
    public Article update(@NonNull String id, @NonNull UpdateArticleCommand command) {
        Article article = articleRepository.getReferenceById(id);
        article.update(command.title(), command.content());
        Category category = article.getCategory();
        categoryRepository.save(category);
        return article;
    }

    @Override
    public String delete(@NonNull String id) {
        Article article = articleRepository.getReferenceById(id);
        String categoryId = article.getCategory().getId();
        // TODO Removal propagate
        articleRepository.delete(article);
        return categoryId;

    }
}
