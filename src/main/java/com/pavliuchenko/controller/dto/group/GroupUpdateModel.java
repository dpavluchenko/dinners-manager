package com.pavliuchenko.controller.dto.group;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupUpdateModel {
    private Long groupId;
    private String name;
    private String dinnerTime;
}
