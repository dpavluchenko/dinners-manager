package com.pavliuchenko.controller.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter @Getter
public class UserRegisterModel {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String fullName;
    @NonNull
    private String role;
}
