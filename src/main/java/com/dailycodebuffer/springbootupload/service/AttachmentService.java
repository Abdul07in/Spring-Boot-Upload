package com.dailycodebuffer.springbootupload.service;

import com.dailycodebuffer.springbootupload.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AttachmentService {


    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

    List<Attachment> getAllFiles();
}
