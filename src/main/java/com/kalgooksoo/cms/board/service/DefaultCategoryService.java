package com.kalgooksoo.cms.board.service;

import com.kalgooksoo.cms.board.command.CreateCategoryCommand;
import com.kalgooksoo.cms.board.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.board.entity.Category;
import com.kalgooksoo.cms.board.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {

    private LocalDateTime refreshTime;

    private final List<Category> categories = new ArrayList<>();

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        refresh();
    }

    @Cacheable("categories")
    @Transactional(readOnly = true)
    @Override
    public void refresh() {
        this.categories.clear();
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));
        this.categories.addAll(categories);
        refreshTime = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getRefreshTime() {
        return refreshTime;
    }

    @Override
    public Category create(@NonNull CreateCategoryCommand command) {
        Category category = Category.create(command);
        Category saved = categoryRepository.save(category);
        refresh();
        return saved;
    }

    @Override
    public List<Category> findAll() {
        return categories;
    }

    @Override
    public Category find(@NonNull String id) {
        return categoryRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Category update(@NonNull String id, @NonNull UpdateCategoryCommand command) {
        Category category = categoryRepository.getReferenceById(id);
        category.update(command);
        Category saved = categoryRepository.save(category);
        refresh();
        return saved;
    }

    @Transactional
    @Override
    public void delete(@NonNull String id) {
        categoryRepository.deleteById(id);
    }

}
