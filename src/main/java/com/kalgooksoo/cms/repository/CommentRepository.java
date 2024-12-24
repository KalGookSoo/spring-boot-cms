package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Reply, String> {
}
