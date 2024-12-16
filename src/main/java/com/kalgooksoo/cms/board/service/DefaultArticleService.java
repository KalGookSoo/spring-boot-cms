package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.command.UpdateArticleCommand;
import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.entity.Attachment;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.repository.ArticleRepository;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
import com.kalgooksoo.cms.board.search.ArticleSearch;
import com.kalgooksoo.core.file.FileIOService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DefaultArticleService implements ArticleService {

    @Value("${com.kalgooksoo.cms.filepath}")
    private final String filepath;

    private final CategoryRepository categoryRepository;

    private final ArticleRepository articleRepository;

    public DefaultArticleService(
            @Value("${com.kalgooksoo.cms.filepath}") String filepath,
            CategoryRepository categoryRepository,
            ArticleRepository articleRepository
    ) {
        this.filepath = filepath;
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Page<Article> findAll(@NonNull ArticleSearch search) {
        return articleRepository.searchAll(search);
    }

    @Transactional
    @Override
    public Article create(@NonNull CreateArticleCommand command) throws IOException {
        Category category = categoryRepository.getReferenceById(command.getCategoryId());
        Article article = Article.create(command);
        for (MultipartFile multipartFile : command.getMultipartFiles()) {
            Attachment attachment = Attachment.create(filepath, multipartFile);
            article.addAttachment(attachment);
            writeFile(attachment.getAbsolutePath(), multipartFile.getBytes());
        }
        category.addArticle(article);
        categoryRepository.save(category);
        return article;
    }

    @Transactional(readOnly = true)
    @Override
    public Article find(@NonNull String id) {
        return articleRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Article update(@NonNull String id, @NonNull UpdateArticleCommand command) {
        Article article = articleRepository.getReferenceById(id);
        article.update(command.getTitle(), command.getContent());
        Category category = article.getCategory();
        categoryRepository.save(category);
        return article;
    }

    @Transactional
    @Override
    public String delete(@NonNull String id) {
        Article article = articleRepository.getReferenceById(id);
        String categoryId = article.getCategory().getId();
        // TODO Removal propagate
        articleRepository.delete(article);
        return categoryId;

    }

    @Transactional
    @Override
    public Article addAttachments(@NonNull String id, @NonNull List<MultipartFile> multipartFiles) throws IOException {
        if (multipartFiles.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Article article = articleRepository.getReferenceById(id);
        for (MultipartFile multipartFile : multipartFiles) {
            Attachment attachment = Attachment.create(filepath, multipartFile);
            article.addAttachment(attachment);
            writeFile(attachment.getAbsolutePath(), multipartFile.getBytes());
        }
        return articleRepository.save(article);
    }

    @Transactional
    @Override
    public Article removeAttachment(@NonNull String id, @NonNull String attachmentId) {
        Article article = articleRepository.getReferenceById(id);
        article.getAttachments().removeIf(attachment -> attachment.getId().equals(attachmentId));
        return articleRepository.save(article);
    }

    private static void writeFile(String pathname, byte[] bytes) {
        try {
            FileIOService.write(pathname, bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
