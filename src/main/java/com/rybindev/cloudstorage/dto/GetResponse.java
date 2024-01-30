package com.rybindev.cloudstorage.dto;

import lombok.Value;

import java.util.List;
@Value
public class GetResponse {
    List<ItemView> items;
}
