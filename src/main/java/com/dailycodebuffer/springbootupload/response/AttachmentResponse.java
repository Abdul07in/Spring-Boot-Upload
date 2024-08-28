package com.dailycodebuffer.springbootupload.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentResponse {
    private String fileId;
    private String fileViewUrl;
    private String fileDownloadUrl;
}
