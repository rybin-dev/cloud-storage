package com.rybindev.cloudstorage.dto;

import lombok.Value;

@Value
public class SearchRequest {
    String query;
    String userId;
}
