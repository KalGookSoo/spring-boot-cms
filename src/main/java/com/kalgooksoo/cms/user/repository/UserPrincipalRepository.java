package com.kalgooksoo.cms.user.repository;

import com.kalgooksoo.cms.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserPrincipalRepository extends Repository<User, String> {

    @EntityGraph(attributePaths = {"authorities"})
    Optional<User> findByUsername(@NonNull String username);

}
