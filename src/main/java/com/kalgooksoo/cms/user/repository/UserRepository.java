package com.kalgooksoo.cms.user.repository;

import com.kalgooksoo.cms.user.entity.User;
import com.kalgooksoo.cms.user.search.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(@NonNull String username);

    default Page<User> searchAll(@NonNull UserSearch search, @NonNull Pageable pageable) {
        Specification<User> specification = Specification.where(null);

        if (!search.isEmptyUsername()) {
            specification = specification.and(usernameContains(search.getUsername()));
        }
        if (!search.isEmptyName()) {
            specification = specification.and(nameContains(search.getName()));
        }
        if (!search.isEmptyEmailId()) {
            specification = specification.and(emailIdContains(search.getEmailId()));
        }
        if (!search.isEmptyContactNumber()) {
            specification = specification.and(contactNumberContains(search.getContactNumber()));
        }

        return findAll(specification, pageable);
    }

    default Specification<User> usernameContains(@NonNull String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), "%" + username + "%");
    }

    default Specification<User> nameContains(@NonNull String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    default Specification<User> emailIdContains(@NonNull String emailId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("emailId"), "%" + emailId + "%");
    }

    default Specification<User> contactNumberContains(@NonNull String contactNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("contactNumber"), "%" + contactNumber + "%");
    }

}
