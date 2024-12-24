package com.kalgooksoo.cms.repository;

import com.kalgooksoo.cms.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
}
