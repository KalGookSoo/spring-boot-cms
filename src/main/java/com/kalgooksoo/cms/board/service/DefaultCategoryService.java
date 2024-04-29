package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category create(CreateCategoryCommand command) {
        if (command.parentId() == null || command.parentId().isEmpty()) {
            Category category = Category.create(null, command.name(), command.type());
            return categoryRepository.save(category);
        } else {
            Category parent = categoryRepository.getReferenceById(command.parentId());
            Category category = Category.create(parent, command.name(), command.type());
            categoryRepository.save(category);
            return category;
        }
    }

    @Override
    public List<Category> findAllByParentIsNull() {
        return categoryRepository.findAllByParentIsNull();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category find(String id) {
        return categoryRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void update(String id, UpdateCategoryCommand command) {
        Category category = categoryRepository.getReferenceById(id);
        if (command.parentId() == null || command.parentId().isEmpty()) {
            category.update(null, command.name(), command.type());
        } else {
            Category parent = categoryRepository.getReferenceById(command.parentId());
            category.update(parent, command.name(), command.type());
        }
        categoryRepository.save(category);
    }

    @Override
    public void delete(String id) {
        categoryRepository.deleteById(id);
    }
}
