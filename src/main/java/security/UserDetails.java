package security;

import domain.User;
import domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDetails {
    private Long userId;
    private String fullName;
    private UserRole role;

    public UserDetails(User user) {
        this.userId = user.getId();
        this.fullName = user.getFullName();
        this.role = user.getRole();
    }
}
