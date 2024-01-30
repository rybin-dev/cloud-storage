package com.rybindev.cloudstorage.service;

import com.rybindev.cloudstorage.dto.StorageItem;
import com.rybindev.cloudstorage.util.PathUtils;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RemoteStorageService {

    private static final String DELIMITER = "/";
    private static final String SUFFIX_FILE = "f";
    private static final String SUFFIX_EMPTY = "e";

    @Value("${minio.bucket.name}")
    private String bucketName;

    private final MinioClient minioClient;

    public void uploadFile(String path, InputStream data) {
        upload(filePath(path), data);
    }

    private void upload(String path, InputStream data) {
        try (InputStream inputStream = data) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .stream(inputStream, -1, 1024 * 1024 * 10)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream downloadFile(String path) {
        return download(filePath(path));
    }

    private @NotNull String filePath(String path) {
        return path + SUFFIX_FILE;
    }

    @SneakyThrows
    public InputStream download(String path) {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(path)
                .build());
    }

    public void renameFile(String path, String newPath) {
        copy(filePath(path), filePath(newPath));
        delete(filePath(path));
    }

    private void rename(String path, String newPath) {
        copy(path, newPath);
        delete(path);
    }

    @SneakyThrows
    private void copy(String oldName, String newName) {
        minioClient.copyObject(
                CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newName)
                        .source(
                                CopySource.builder()
                                        .bucket(bucketName)
                                        .object(oldName)
                                        .build())
                        .build());
    }

    public void deleteFile(String path) {
        delete(filePath(path));
        upload(PathUtils.replaceName(path, SUFFIX_EMPTY), emptyInputStream());
    }

    @SneakyThrows
    private void delete(String path) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(path)
                .build());
    }

    public void createDirectory(String path) {
        upload(directoryPath(path) + SUFFIX_EMPTY, emptyInputStream());
    }

    private @NotNull String directoryPath(String path) {
        return path + DELIMITER;
    }

    public void deleteDirectory(String path) {
        upload(path, emptyInputStream());
        rename(path, PathUtils.replaceName(path, SUFFIX_EMPTY));
    }

    private @NotNull ByteArrayInputStream emptyInputStream() {
        return new ByteArrayInputStream(new byte[0]);
    }

    public void renameDirectory(String path, String newPath) {
        itemStream(directoryPath(path), true).forEach(file ->
                copy(file.objectName(),
                        file.objectName().replace(directoryPath(path), directoryPath(newPath))));

        deleteDirectory(path);
    }

    public List<StorageItem> getAll(String path) {
        return getAll(path, false);
    }

    public List<StorageItem> getAll(String path, boolean isRecursive) {
        return itemStream(directoryPath(path), isRecursive)
                .filter(item -> !item.objectName().endsWith(SUFFIX_EMPTY))
                .map(this::mapToStorageItem)
                .toList();
    }

    private @NotNull StorageItem mapToStorageItem(Item item) {
        return new StorageItem(item.objectName().substring(0,item.objectName().length()-1), item.isDir());
    }


    private Stream<Item> itemStream(String path, boolean isRecursive) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path)
                .recursive(isRecursive)
                .build());
        return StreamSupport.stream(results.spliterator(), false)
                .map(this::getItem);
    }

    private Item getItem(Result<Item> itemResult) {
        try {
            return itemResult.get();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }



}
