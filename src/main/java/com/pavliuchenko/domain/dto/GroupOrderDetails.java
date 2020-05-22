package com.pavliuchenko.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
public class GroupOrderDetails {
   private String fullName;
   private Map<String, Boolean> chooseByType;
}
