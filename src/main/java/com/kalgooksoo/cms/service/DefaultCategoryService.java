package com.kalgooksoo.cms.service;

import com.kalgooksoo.cms.command.CreateCategoryCommand;
import com.kalgooksoo.cms.command.UpdateCategoryCommand;
import com.kalgooksoo.cms.entity.Category;
import com.kalgooksoo.cms.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Category create(@NonNull CreateCategoryCommand command) {
        Category parent = command.parentId() == null ? null : categoryRepository.getReferenceById(command.parentId());
        Category category = Category.create(command, parent);
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));
    }

    @Transactional(readOnly = true)
    @Override
    public Category find(@NonNull String id) {
        return categoryRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    @Override
    public Category update(@NonNull String id, @NonNull UpdateCategoryCommand command) {
        Category parent = command.parentId() == null ? null : categoryRepository.getReferenceById(command.parentId());
        Category category = categoryRepository.getReferenceById(id);
        category.update(command, parent);
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public void delete(@NonNull String id) {
        categoryRepository.deleteById(id);
    }

}
