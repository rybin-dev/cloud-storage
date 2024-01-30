package com.rybindev.cloudstorage.dto;

import lombok.Value;

@Value
public class DownloadRequest {
    String path;
    String userId;
}
