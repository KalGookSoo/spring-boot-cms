package com.kalgooksoo.cms.board.repository;

import com.kalgooksoo.cms.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
