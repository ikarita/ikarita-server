package com.github.ikarita.server.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ikarita_community_user")
public class CommunityUser {
    @EmbeddedId
    private CommunityUserId developerProjectId;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("communityId")
    private Community community;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<CommunityRole> communityRoles;
}
