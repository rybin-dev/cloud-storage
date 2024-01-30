package com.rybindev.cloudstorage.dto;

import com.rybindev.cloudstorage.util.PathUtils;
import lombok.Value;

@Value
public class StorageItem {
    String path;
    boolean isDir;

    public String getName() {
        return PathUtils.extractName(path);
    }
}
