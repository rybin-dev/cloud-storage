package com.rybindev.cloudstorage.service;

import com.rybindev.cloudstorage.dto.*;
import com.rybindev.cloudstorage.util.PathUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StorageService {

    private static final String DELIMITER = "/";
    private static final String PREFIX_USER = "user-";

    private final RemoteStorageService remoteStorage;

    public GetResponse getFiles(GetRequest request) {
        String path = path(request.getUserId(), request.getPath());
        return new GetResponse(getAll(path));
    }

    public void uploadFile(UploadRequest request) {
        request.getFiles().forEach(file -> {
            String path = path(request.getUserId(), request.getPath(), file.getOriginalFilename());
            try {
                remoteStorage.uploadFile(path, file.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public DownloadResponse downloadFile(DownloadRequest request) {
        String path = path(request.getUserId(), request.getPath());
        return new DownloadResponse(PathUtils.extractName(request.getPath()), remoteStorage.downloadFile(path));
    }

    public void deleteFile(DeleteRequest request) {
        String path = path(request.getUserId(), request.getPath());
        remoteStorage.deleteFile(path);
    }

    public void renameFile(RenameRequest request) {
        String path = path(request.getUserId(), request.getPath());
        String newPath = path(request.getUserId(), PathUtils.replaceName(request.getPath(), request.getNewName()));
        remoteStorage.renameFile(path, newPath);
    }

    public void createDirectory(CreateDirectoryRequest request) {
        String path = path(request.getUserId(), request.getPath(), request.getName());
        remoteStorage.createDirectory(path);
    }

    public void renameDirectory(RenameRequest request) {
        String path = path(request.getUserId(), request.getPath());
        String newPath = path(request.getUserId(), PathUtils.replaceName(request.getPath(), request.getNewName()));

        remoteStorage.renameDirectory(path,newPath);
    }

    public void deleteDirectory(DeleteRequest request) {
        String path = path(request.getUserId(), request.getPath());
        remoteStorage.deleteDirectory(path);
    }

    private String path(String... arr) {
        return Arrays.stream(arr)
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.joining(DELIMITER, PREFIX_USER, ""));
    }

    private List<ItemView> getAll(String path) {
        return remoteStorage.getAll(path).stream()
                .map(this::itemView)
                .collect(Collectors.toList());

    }

    private @NotNull ItemView itemView(StorageItem storageItem) {
        String path = storageItem.getPath();
        String pathView = path.substring(path.indexOf(DELIMITER) + 1);
        return new ItemView(pathView, storageItem.isDir());
    }

    public List<ItemView> search(SearchRequest request) {
        String path = path(request.getUserId());

        return remoteStorage.getAll(path, true).stream()
                .filter(storageItem -> contains(request.getQuery(), storageItem))
                .map(this::itemView)
                .collect(Collectors.toList());

    }

    private boolean contains(String query, StorageItem storageItem) {
        String path = storageItem.getPath();
        String lowerCase = path.substring(path.lastIndexOf(DELIMITER) + 1).toLowerCase();
        return lowerCase.contains(query.toLowerCase());
    }
}
