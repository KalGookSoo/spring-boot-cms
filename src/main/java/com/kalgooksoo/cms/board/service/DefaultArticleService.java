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

    private final String filepath;

    private final CategoryRepository categoryRepository;

    private final ArticleRepository articleRepository;

    private final Tika tika = new Tika();

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
        Article article = Article.create(command.getTitle(), command.getContent());

        List<MultipartFile> multipartFiles = command.getMultipartFiles()
                .stream()
                .filter(file -> !file.isEmpty())
                .toList();
        for (MultipartFile multipartFile : multipartFiles) {
            detectMimeType(multipartFile);
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
            detectMimeType(multipartFile);
            Attachment attachment = Attachment.create(filepath, multipartFile);
            article.addAttachment(attachment);
            writeFile(attachment.getAbsolutePath(), multipartFile.getBytes());
        }
        articleRepository.save(article);
        return article;
    }

    private void detectMimeType(MultipartFile multipartFile) throws IOException {
        String mimeType = tika.detect(multipartFile.getInputStream());
        if (!mimeType.equals(multipartFile.getContentType())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Detected MIME type does not match the provided content type.").append(System.lineSeparator())
                    .append("Detected MIME Type: ").append(mimeType).append(System.lineSeparator())
                    .append("Attached Content Type: ").append(multipartFile.getContentType()).append(System.lineSeparator());
            throw new IllegalArgumentException(sb.toString());
        }
    }

    private static void writeFile(String pathname, byte[] bytes) {
        try {
            FileIOService.write(pathname, bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
