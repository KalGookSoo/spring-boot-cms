package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category create(@NonNull CreateCategoryCommand command) {
        String parentId = command.parentId();
        Category parent = Optional.ofNullable(parentId)
                .filter(value -> !value.isEmpty())
                .map(categoryRepository::getReferenceById)
                .orElse(null);
        Category category = Category.create(parent, command.name(), command.type());
        return categoryRepository.save(category);
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
    public Category find(@NonNull String id) {
        return categoryRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Category update(@NonNull String id, @NonNull UpdateCategoryCommand command) {
        Category category = categoryRepository.getReferenceById(id);
        String parentId = command.parentId();
        Category parent = Optional.ofNullable(parentId)
                .filter(value -> !value.isEmpty())
                .map(categoryRepository::getReferenceById)
                .orElse(null);

        category.update(parent, command.name(), command.type());
        return categoryRepository.save(category);
    }

    @Override
    public void delete(@NonNull String id) {
        categoryRepository.deleteById(id);
    }
}
