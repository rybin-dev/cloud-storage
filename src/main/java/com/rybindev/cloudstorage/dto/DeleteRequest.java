package com.rybindev.cloudstorage.dto;

import lombok.Value;

@Value
public class DeleteRequest {
    String path;
    String userId;
}
