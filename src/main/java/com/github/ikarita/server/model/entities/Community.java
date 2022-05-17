package com.github.ikarita.server.model.entities;

import com.github.ikarita.server.model.entities.geo.DataType;
import com.github.ikarita.server.model.entities.geo.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ikarita_community")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Unique
    private String name;



    @Column(columnDefinition = "boolean default true")
    private boolean isPublic;

    @Column(columnDefinition = "boolean default true")
    private boolean isActive;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private Set<CommunityUser> users;

    @OneToMany(mappedBy = "community")
    private List<GeoPoint> geoPoints;

    @ManyToOne
    private DataType dataType;
}
