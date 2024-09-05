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

    private final Collection<Category> categories = new LinkedHashSet<>();

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        List<Category> categories = categoryRepository.findAll();
        this.categories.addAll(categories);
    }

    @Override
    public Category create(@NonNull CreateCategoryCommand command) {
        String parentId = command.parentId();
        Category parent = Optional.ofNullable(parentId)
                .filter(value -> !value.isEmpty())
                .map(categoryRepository::getReferenceById)
                .orElse(null);
        Category category = Category.create(parent, command.name(), command.type());
        Category saved = categoryRepository.save(category);
//        findAll().add(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Category> findAllNested() {
        Collection<Category> categories = findAll();
        Map<Category, List<Category>> collect = categories.stream()
                .filter(Category::hasParent)
                .collect(Collectors.groupingBy(Category::getParent));

        return categories.stream()
                .filter(Category::isRoot)
                .map(category -> category.mapChildren(category, collect))
                .toList();
    }

//    @Cacheable("categories")
    @Override
    public Collection<Category> findAll() {
//        return categories;
        return categoryRepository.findAll();
    }

    @Override
    public Category find(@NonNull String id) {
        return categoryRepository.findById(id).orElseThrow(NoSuchElementException::new);
//        return categories.stream()
//                .filter(category -> id.equals(category.getId()))
//                .findFirst()
//                .orElseThrow(NoSuchElementException::new);
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
        Category saved = categoryRepository.save(category);
//        findAll().add(saved);
        return saved;
    }

    @Transactional
    @Override
    public void delete(@NonNull String id) {
        categoryRepository.deleteById(id);
    }
}
