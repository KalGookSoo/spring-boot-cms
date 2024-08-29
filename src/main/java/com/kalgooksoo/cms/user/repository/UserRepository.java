package com.kalgooksoo.cms.user.repository;

import com.kalgooksoo.cms.user.entity.User;
import com.kalgooksoo.cms.user.search.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

/**
 * 계정 저장소
 */
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 계정 검색 조건에 해당되는 계정 목록을 반환합니다.
     * @param search   계정 검색 조건
     * @param pageable 페이지네이션 요청 정보
     * @return 계정 목록
     */
    default Page<User> searchAll(@NonNull UserSearch search, @NonNull Pageable pageable) {
        Specification<User> specification = Specification.where(null);

        if (!search.isEmptyUsername()) {
            specification = specification.and(usernameContains(search.getUsername()));
        }
        if (!search.isEmptyName()) {
            specification = specification.and(nameContains(search.getName()));
        }
//        if (search.getEmail() != null) {
//            specification = specification.and(emailContains(search.getEmail()));
//        }
//        if (!search.isEmptyContactNumber()) {
//            specification = specification.and(contactNumberContains(search.getContactNumber()));
//        }

        return findAll(specification, pageable);
    }

    private Specification<User> usernameContains(@NonNull String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), "%" + username + "%");
    }

    private Specification<User> nameContains(@NonNull String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

//    private Specification<User> emailContains(@NonNull Email email) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), "%" + email + "%");
//    }

//    private Specification<User> contactNumberContains(@NonNull ContactNumber contactNumber) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("contactNumber"), "%" + contactNumber.getValue() + "%");
//    }

}
