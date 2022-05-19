package com.github.ikarita.server.model.entities.user;

import com.github.ikarita.server.model.entities.community.Community;
import com.github.ikarita.server.model.entities.community.CommunityRole;
import com.github.ikarita.server.model.entities.community.CommunityUserId;
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
    @JoinTable(name = "ikarita_community_user_role")
    private Collection<CommunityRole> communityRoles;
}
