package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.entity.CategoryType;
import com.kalgooksoo.cms.board.repository.ArticleRepository;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
class ArticleServiceTest {

    private CategoryService categoryService;

    private ArticleService articleService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup(@TempDir Path tempDir) {
        categoryService = new DefaultCategoryService(categoryRepository);
        articleService = new DefaultArticleService(tempDir.toString(), categoryRepository, articleRepository);
    }

    @Test
    @DisplayName("게시글 생성 - ")
    void createArticle() {
        // Given
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(null, "카테고리", CategoryType.PUBLIC);
        Category createdCategory = categoryService.create(createCategoryCommand);
        entityManager.flush();
        CreateArticleCommand command = new CreateArticleCommand(createdCategory.getId());
        command.setTitle("title");
        command.setContent("content");
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile multipartFile1 = new MockMultipartFile("name", "연차현황.png", "image/png", "Some image data".getBytes());
        multipartFiles.add(multipartFile1);
        command.setMultipartFiles(multipartFiles);

        // When
        Article article = articleService.create(command);
        entityManager.flush();

        // Then
        Assertions.assertNotNull(article.getId());
        Assertions.assertNotNull(article.getAttachments().iterator().next().getId());
    }

}