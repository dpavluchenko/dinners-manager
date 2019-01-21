package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

@Getter @Setter
@NoArgsConstructor
public class User extends Entity{
    private String username;
    private String password;
    private String fullName;
    private UserRole role;
    private Long groupId;

    public User(String username, String password, String fullName, String role, long groupId) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.fullName = fullName;
        this.role = UserRole.valueOf(role);
        this.groupId = groupId;
    }

    public boolean isPresent() {
        return id != null;
    }

    public boolean checkPasswordIdentity(String passForCheck){
       return BCrypt.checkpw(passForCheck, password);
    }
}
