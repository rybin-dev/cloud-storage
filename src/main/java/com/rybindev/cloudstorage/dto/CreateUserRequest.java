package com.rybindev.cloudstorage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class CreateUserRequest {
    @NotNull
    @NotBlank
    @Size(min= 1,max = 64)
    private String username;

    @NotNull
    @NotBlank
    @Size(min=3)
    private String password;
}
