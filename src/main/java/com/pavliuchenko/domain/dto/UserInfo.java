package com.pavliuchenko.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String fullName;
    private String groupName;
    private String dinnerTime;
}
