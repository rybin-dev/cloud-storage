package com.rybindev.cloudstorage.dto;

import com.rybindev.cloudstorage.util.PathUtils;
import lombok.Value;

@Value
public class ItemView {
    String path;
    boolean isDir;

    public String getName() {
        return PathUtils.extractName(path);
    }
}
