package com.github.ikarita.server.model.entities.community;

import com.github.ikarita.server.security.permissions.CommunityPermission;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ikarita_community_role")
public class CommunityRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Collection<CommunityPermission> permissions = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;
}
