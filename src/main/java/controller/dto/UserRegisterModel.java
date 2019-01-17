package controller.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter @Getter
public class UserRegisterModel {
    @NonNull
    private String username;
    private String password;
    private String fullName;
    private String role;
}
