package com.rybindev.cloudstorage.dto;

import lombok.Value;

@Value
public class CreateDirectoryRequest {
    String path;
    String userId;
    String name;
}
