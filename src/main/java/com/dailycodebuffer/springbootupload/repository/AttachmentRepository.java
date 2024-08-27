package com.dailycodebuffer.springbootupload.repository;

import com.dailycodebuffer.springbootupload.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {
}
