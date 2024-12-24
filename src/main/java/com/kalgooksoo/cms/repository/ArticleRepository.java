package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.Article;
import com.kalgooksoo.cms.search.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

    default Page<Article> searchAll(ArticleSearch search) {
        Sort sort = Sort.by(Sort.Direction.fromString(search.getSortDirection()), search.getSort());
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), sort);

        Specification<Article> specification = Specification.where(null);

        specification = specification.and(categoryIdEquals(search.getCategoryId()));

        if (!search.isEmptyTitle()) {
            specification = specification.and(titleContains(search.getTitle()));
        }
        if (!search.isEmptyContent()) {
            specification = specification.and(contentContains(search.getContent()));
        }
        return findAll(specification, pageable);
    }

    default Specification<Article> titleContains(@NonNull String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    default Specification<Article> contentContains(@NonNull String content) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("content"), "%" + content + "%");
    }

    default Specification<Article> categoryIdEquals(@NonNull String categoryId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    @EntityGraph(attributePaths = {"category", "replies", "votes", "views", "attachments"})
    Optional<Article> findById(@NonNull String id);
}
