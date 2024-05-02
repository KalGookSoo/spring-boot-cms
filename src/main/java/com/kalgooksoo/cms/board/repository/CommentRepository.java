package com.kalgooksoo.cms.board.repository;

import com.kalgooksoo.cms.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Reply, String> {
}
