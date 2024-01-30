package com.rybindev.cloudstorage.dto;

import lombok.Value;

@Value
public class RenameRequest {
    String path;
    String userId;
    String newName;
}
