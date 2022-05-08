package com.github.ikarita.server.model.dto;

import com.github.ikarita.server.model.entities.CommunityRole;
import com.github.ikarita.server.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Collection<UserRole> userRoles = new ArrayList<>();
    private Collection<CommunityRole> communityRoles = new ArrayList<>();
}
