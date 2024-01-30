package com.rybindev.cloudstorage.dto;

import lombok.Value;

@Value
public class GetRequest {
    String path;
    String userId;
}
