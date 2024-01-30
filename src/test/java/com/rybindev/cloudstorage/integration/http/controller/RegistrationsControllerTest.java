package com.rybindev.cloudstorage.integration.http.controller;

import com.rybindev.cloudstorage.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.rybindev.cloudstorage.dto.CreateUserRequest.Fields.password;
import static com.rybindev.cloudstorage.dto.CreateUserRequest.Fields.username;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class RegistrationsControllerTest extends IntegrationTestBase {
    private final MockMvc mockMvc;


    @Test
    void shouldOpenTheRegistrationPage() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldRegisterAndRedirectToTheLoginPage() throws Exception {
        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param(username, "username")
                        .param(password, "password")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/login")
                );
    }




}
