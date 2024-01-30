package com.rybindev.cloudstorage.controller;

import com.rybindev.cloudstorage.dto.*;
import com.rybindev.cloudstorage.service.StorageService;
import com.rybindev.cloudstorage.validator.Path;
import com.rybindev.cloudstorage.validator.UploadFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final StorageService storageService;

    @PostMapping()
    public String upload(@RequestParam(required = false, defaultValue = "") @Path String path,
                         @Size(min = 1) List<@UploadFile MultipartFile> files,
                         @AuthenticationPrincipal AuthorizedUser user,
                         RedirectAttributes redirectAttributes) {
        UploadRequest request = new UploadRequest(path, user.getId().toString(), files);
        storageService.uploadFile(request);
        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(required = false, defaultValue = "") @Path String parent,
                         @RequestParam @Path String path,
                         @AuthenticationPrincipal AuthorizedUser user,
                         RedirectAttributes redirectAttributes) {
        DeleteRequest request = new DeleteRequest(path, user.getId().toString());
        storageService.deleteFile(request);
        redirectAttributes.addAttribute("path", parent);
        return "redirect:/";
    }

    @PostMapping("/rename")
    public String rename(@RequestParam(required = false, defaultValue = "") @Path String parent,
                         @RequestParam @Path @NotBlank String newName,
                         @RequestParam @Path String path,
                         @AuthenticationPrincipal AuthorizedUser user,
                         RedirectAttributes redirectAttributes) {
        RenameRequest request = new RenameRequest(path, user.getId().toString(), newName);
        storageService.renameFile(request);
        redirectAttributes.addAttribute("path", parent);
        return "redirect:/";
    }

    @GetMapping()
    public ResponseEntity<? extends Resource> download(@RequestParam @Path String path,
                                                       @AuthenticationPrincipal AuthorizedUser user) {
        DownloadRequest request = new DownloadRequest(path, user.getId().toString());
        DownloadResponse response = storageService.downloadFile(request);

        InputStreamResource inputStreamResource = new InputStreamResource(response.getInputStream());
        return ResponseEntity.ok()
                .headers(prepareHeaders(URLEncoder.encode(response.getFileName(), StandardCharsets.UTF_8)))
                .body(inputStreamResource);
    }

    private HttpHeaders prepareHeaders(String fileName) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
        httpHeaders.add("Content-Type", URLConnection.guessContentTypeFromName(fileName));
        return httpHeaders;

    }

}
