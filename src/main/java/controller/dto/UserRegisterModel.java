package controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserRegisterModel {
    private String username;
    private String password;
    private String fullName;
    private String role;
}
