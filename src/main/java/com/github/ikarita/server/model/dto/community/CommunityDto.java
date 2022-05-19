package com.github.ikarita.server.model.dto.community;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDto {
    private Long id;
    private String name;
    private boolean isPublic;
}
