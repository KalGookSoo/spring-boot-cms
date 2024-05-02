package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.entity.CategoryType;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CategoryServiceTest {

    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

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
        entityManager.flush();

        // Then
        assertNotNull(createdCategory.getId());
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
        entityManager.flush();

        // Then
        assertEquals(createdParentCategory.getId(), createdCategory.getParent().getId());
    }

    @Test
    @DisplayName("카테고리 수정 - 수정된 카테고리 반환")
    void updateShouldReturnCategory() {
        // Given
        CreateCategoryCommand createParentCategoryCommand = new CreateCategoryCommand(null, "부모카테고리", CategoryType.PUBLIC);
        Category createdParentCategory = categoryService.create(createParentCategoryCommand);
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(createdParentCategory.getId(), "카테고리", CategoryType.PUBLIC);
        Category createdCategory = categoryService.create(createCategoryCommand);
        entityManager.flush();
        UpdateCategoryCommand updateCategoryCommand = new UpdateCategoryCommand(null, "수정된 카테고리", CategoryType.PRIVATE);

        // When
        Category updatedCategory = categoryService.update(createdCategory.getId(), updateCategoryCommand);
        entityManager.flush();

        // Then
        assertNull(updatedCategory.getParent());
        assertEquals("수정된 카테고리", updatedCategory.getName());
        assertEquals(CategoryType.PRIVATE, updatedCategory.getType());
    }

    @Test
    @DisplayName("카테고리 삭제")
    void delete() {
        // Given
        CreateCategoryCommand createParentCategoryCommand = new CreateCategoryCommand(null, "부모카테고리", CategoryType.PUBLIC);
        Category createdParentCategory = categoryService.create(createParentCategoryCommand);
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(createdParentCategory.getId(), "카테고리", CategoryType.PUBLIC);
        Category createdCategory = categoryService.create(createCategoryCommand);
        entityManager.flush();
        entityManager.clear();

        // When & Then
        categoryService.delete(createdParentCategory.getId());
        assertThrows(ConstraintViolationException.class, () -> entityManager.flush());
        categoryService.delete(createdCategory.getId());
        entityManager.flush();
        assertThrows(NoSuchElementException.class, () -> categoryService.find(createdCategory.getId()));
    }

}