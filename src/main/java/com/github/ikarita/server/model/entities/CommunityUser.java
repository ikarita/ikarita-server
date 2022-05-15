package com.github.ikarita.server.model.entities;

import lombok.*;

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
    private CommunityUserId communityUserId;

    @ManyToOne
    @MapsId("userId")
    private LocalUser localUser;

    @ManyToOne
    @MapsId("communityId")
    private Community community;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<CommunityRole> communityRoles;
}
