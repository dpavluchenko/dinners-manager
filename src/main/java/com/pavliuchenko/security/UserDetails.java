package com.pavliuchenko.security;

import com.pavliuchenko.domain.User;
import com.pavliuchenko.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDetails {
    private Long userId;
    private String fullName;
    private UserRole role;
    private Long groupId;

    public UserDetails(User user) {
        this.userId = user.getId();
        this.fullName = user.getFullName();
        this.role = user.getRole();
        this.groupId = user.getGroupId();
    }
}
