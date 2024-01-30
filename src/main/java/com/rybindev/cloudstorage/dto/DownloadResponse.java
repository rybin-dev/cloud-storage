package com.rybindev.cloudstorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class DownloadResponse {
    private String fileName;
    private InputStream inputStream;
}
