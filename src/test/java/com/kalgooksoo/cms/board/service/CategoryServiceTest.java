package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.entity.CategoryType;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class CategoryServiceTest {

    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        categoryService = new DefaultCategoryService(categoryRepository);
    }

    @Test
    @DisplayName("카테고리 생성 - 카테고리 반환")
    void createShouldReturnCategory() {
        // Given
        CreateCategoryCommand command = new CreateCategoryCommand(null, "카테고리", CategoryType.PUBLIC);

        // When
        Category createdCategory = categoryService.create(command);

        // Then
        assertEquals("카테고리", createdCategory.getName());
        assertEquals(CategoryType.PUBLIC, createdCategory.getType());
    }

    @Test
    @DisplayName("카테고리 생성 - 카테고리 & 부모 카테고리 반환")
    void createShouldReturnCategoryWithParent() {
        // Given
        CreateCategoryCommand createParentCategoryCommand = new CreateCategoryCommand(null, "부모카테고리", CategoryType.PUBLIC);
        Category createdParentCategory = categoryService.create(createParentCategoryCommand);
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(createdParentCategory.getId(), "부모카테고리", CategoryType.PUBLIC);

        // When
        Category createdCategory = categoryService.create(createCategoryCommand);

        // Then
        assertEquals(createdParentCategory.getId(), createdCategory.getParent().getId());
    }

}