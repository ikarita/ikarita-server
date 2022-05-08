package com.github.ikarita.server.model.entities;

import com.github.ikarita.server.security.UserRole;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Unique
    private String username;
    @Unique
    private String email;
    private String password;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<UserRole> userRoles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<CommunityUser> communities;
}
