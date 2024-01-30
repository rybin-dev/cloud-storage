package com.rybindev.cloudstorage.controller;

import com.rybindev.cloudstorage.dto.CreateDirectoryRequest;
import com.rybindev.cloudstorage.dto.AuthorizedUser;
import com.rybindev.cloudstorage.dto.DeleteRequest;
import com.rybindev.cloudstorage.dto.RenameRequest;
import com.rybindev.cloudstorage.service.StorageService;
import com.rybindev.cloudstorage.validator.Path;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/directories")
public class DirectoryController {

    private final StorageService storageService;

    @PostMapping("/delete")
    public String delete(@RequestParam(required = false, defaultValue = "") @Path String parent,
                         @RequestParam @Path String path,
                         @AuthenticationPrincipal AuthorizedUser user,
                         RedirectAttributes redirectAttributes) {
        DeleteRequest request = new DeleteRequest(path, user.getId().toString());
        storageService.deleteDirectory(request);
        redirectAttributes.addAttribute("path", parent);
        return "redirect:/";
    }

    @PostMapping("/rename")
    public String rename(@RequestParam(required = false, defaultValue = "") @Path String parent,
                         @RequestParam @Path String path,
                         @RequestParam @Path @NotBlank String newName,
                         @AuthenticationPrincipal AuthorizedUser user,
                         RedirectAttributes redirectAttributes) {
        RenameRequest request = new RenameRequest(path, user.getId().toString(), newName);
        storageService.renameDirectory(request);
        redirectAttributes.addAttribute("path", parent);
        return "redirect:/";
    }

    @PostMapping("/add")
    public String add(@RequestParam(required = false, defaultValue = "") @Path String path,
                      @RequestParam @Path @NotBlank String name,
                      @AuthenticationPrincipal AuthorizedUser user,
                      RedirectAttributes redirectAttributes) {
        CreateDirectoryRequest request = new CreateDirectoryRequest(path, user.getId().toString(), name);
        storageService.createDirectory(request);
        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }


}
