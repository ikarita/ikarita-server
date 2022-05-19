package com.github.ikarita.server.model.entities.user;

import com.github.ikarita.server.security.permissions.UserRole;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ikarita_user")
public class LocalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Unique
    private String username;
    @Unique
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @OneToMany(mappedBy = "localUser", cascade = CascadeType.ALL)
    private Set<CommunityUser> communities;
    @Column(columnDefinition = "boolean default false")
    private boolean isBanned;
}
