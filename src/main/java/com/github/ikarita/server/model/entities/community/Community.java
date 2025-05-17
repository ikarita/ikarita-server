package com.github.ikarita.server.model.entities.community;

import com.github.ikarita.server.model.entities.data.DataPoint;
import com.github.ikarita.server.model.entities.user.CommunityUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;

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
    private long id;

    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "boolean default true")
    private boolean isPublic;

    @Column(columnDefinition = "boolean default true")
    private boolean isActive;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private Set<CommunityUser> users;

    @OneToMany(mappedBy = "community")
    private List<DataPoint> geoData;

    @JdbcTypeCode(SqlTypes.JSON)
    private Object metadataSchema;
}
