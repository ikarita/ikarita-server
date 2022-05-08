package com.github.ikarita.server.model.entities;

import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ikarita_community")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Unique
    private String name;
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private Set<CommunityUser> communities;
}
