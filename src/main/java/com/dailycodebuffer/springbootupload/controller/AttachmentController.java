package com.dailycodebuffer.springbootupload.controller;

import com.dailycodebuffer.springbootupload.ResponseData;
import com.dailycodebuffer.springbootupload.entity.Attachment;
import com.dailycodebuffer.springbootupload.response.AttachmentResponse;
import com.dailycodebuffer.springbootupload.service.AttachmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseData> uploadFile(@RequestParam("file") MultipartFile file) {
        Attachment attachment = null;
        String downloadURl = "";

        try {
            // Save the file to the database or filesystem
            attachment = attachmentService.saveAttachment(file);

            // Build the download URL
            downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(attachment.getId())
                    .toUriString();
        } catch (Exception e) {
//            e.printStackTrace(); // Or better use a logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData("An error occurred", "", "", 0));
        }

        // Return successful response
        return ResponseEntity.ok(new ResponseData(
                attachment.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize()
        ));
    }

    @GetMapping("/files")
    public List<AttachmentResponse> getFiles(HttpServletRequest request) {
        String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());

        return attachmentService.getAllFiles().stream()
                .map(attachment -> new AttachmentResponse(
                        attachment.getId(),
                        baseUrl + "/download/" + attachment.getId(),
                        baseUrl + "/view/" + attachment.getId()))
                .collect(Collectors.toList());
    }



    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }
}
