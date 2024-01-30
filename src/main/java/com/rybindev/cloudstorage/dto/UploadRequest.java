package com.rybindev.cloudstorage.dto;

import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Value
public class UploadRequest {
    String path;
    String userId;
    List<MultipartFile> files;
}
