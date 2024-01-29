package com.rybindev.cloudstorage.controller;

import com.rybindev.cloudstorage.dto.CreateUserRequest;
import com.rybindev.cloudstorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationsController {
    private final UserService userService;

    @GetMapping
    public String registration(Model model, @ModelAttribute("user") CreateUserRequest request) {
        model.addAttribute("user", request);
        return "registration";
    }

    @PostMapping
    public String register(@ModelAttribute("user") @Validated CreateUserRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", request);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/registration";
        }

        userService.register(request);
        return "redirect:/login";
    }
}
