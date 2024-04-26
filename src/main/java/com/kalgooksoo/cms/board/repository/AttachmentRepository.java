package com.kalgooksoo.cms.board.repository;

import com.kalgooksoo.cms.board.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
}
