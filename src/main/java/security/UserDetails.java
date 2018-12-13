package security;

import domain.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UserDetails {
    private String username;
    private List<UserRole> roles;

    public UserDetails(String username, List<UserRole> roles) {
        this.username = username;
        this.roles = roles;
    }
}
