package com.rybindev.cloudstorage.controller;

import com.rybindev.cloudstorage.dto.AuthorizedUser;
import com.rybindev.cloudstorage.dto.Breadcrumb;
import com.rybindev.cloudstorage.dto.GetRequest;
import com.rybindev.cloudstorage.dto.GetResponse;
import com.rybindev.cloudstorage.service.StorageService;
import com.rybindev.cloudstorage.util.PathUtils;
import com.rybindev.cloudstorage.validator.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StorageService storageService;

    @GetMapping()
    public String get(Model model,
                      @RequestParam(required = false, defaultValue = "") @Path String path,
                      @AuthenticationPrincipal AuthorizedUser user) {
        GetRequest request = new GetRequest(path, user.getId().toString());
        GetResponse response = storageService.getFiles(request);
        model.addAttribute("files", response.getItems());
        model.addAttribute("breadcrumbs", getBreadcrumbs(path));
        model.addAttribute("path", path);
        model.addAttribute("user", user);

        return "home";
    }

    private List<Breadcrumb> getBreadcrumbs(String path) {
     return    PathUtils.getBreadcrumbs(path);
    }


}
