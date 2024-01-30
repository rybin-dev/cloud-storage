package com.rybindev.cloudstorage.controller;

import com.rybindev.cloudstorage.dto.AuthorizedUser;
import com.rybindev.cloudstorage.dto.SearchRequest;
import com.rybindev.cloudstorage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("search")
public class SearchController {
    private final StorageService storageService;

    @GetMapping
    public String search(Model model,
                         @RequestParam(required = false,defaultValue = "") String query,
                         @AuthenticationPrincipal AuthorizedUser user) {
        if (!query.isBlank()){
            SearchRequest request = new SearchRequest(query, user.getId().toString());
            model.addAttribute("files", storageService.search(request));
        }

        model.addAttribute("user", user);
        return "search";
    }

}
