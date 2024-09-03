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
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DefaultArticleService implements ArticleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String filepath;

    private final CategoryRepository categoryRepository;

    private final ArticleRepository articleRepository;

    private final Tika tika = new Tika();

    public DefaultArticleService(
            @Value("${com.kalgooksoo.cms.filepath}") String filepath,
            CategoryRepository categoryRepository,
            ArticleRepository articleRepository
    ) {
        this.filepath = System.getProperty("user.dir") + filepath;
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Page<Article> findAll(@NonNull ArticleSearch search) {
        return articleRepository.searchAll(search);
    }

    @Override
    public Article create(@NonNull CreateArticleCommand command) {
        Category category = categoryRepository.getReferenceById(command.getCategoryId());
        Article article = Article.create(command.getTitle(), command.getContent());

        List<MultipartFile> multipartFiles = command.getMultipartFiles()
                .stream()
                .filter(file -> !file.isEmpty())
                .toList();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                String mimeType = tika.detect(multipartFile.getInputStream());
                Attachment attachment = Attachment.create(multipartFile.getOriginalFilename(), filepath, mimeType, multipartFile.getSize());
                article.addAttachment(attachment);
                String pathname = filepath + File.separator + LocalDateTime.now() + "_" + multipartFile.getOriginalFilename();
                FileIOService.write(pathname, multipartFile.getBytes());

            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
                throw new IllegalArgumentException(e.getLocalizedMessage());
            }
        }
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
