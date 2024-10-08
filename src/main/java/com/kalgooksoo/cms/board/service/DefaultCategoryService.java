package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @PostConstruct
    public void refresh() {

    }

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

    @Transactional(readOnly = true)
    @Override
    public Collection<Category> findAllNested() {
        Collection<Category> categories = findAll();
        Map<Category, List<Category>> collect = categories
                .stream()
                .filter(Category::hasParent)
                .collect(Collectors.groupingBy(Category::getParent));

        return categories.stream()
                .filter(Category::isRoot)
                .map(category -> category.mapChildren(category, collect))
                .toList();
    }

    //    @Cacheable("categories")
    @Transactional(readOnly = true)
    @Override
    public Collection<Category> findAll() {
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

    @Transactional
    @Override
    public void delete(@NonNull String id) {
        categoryRepository.deleteById(id);
    }
}
