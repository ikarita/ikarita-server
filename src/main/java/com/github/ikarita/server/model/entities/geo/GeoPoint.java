package com.github.ikarita.server.model.entities.geo;

import com.github.ikarita.server.model.entities.Community;
import com.github.ikarita.server.model.entities.LocalUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ikarita_geo_point")
public class GeoPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Float north;

    private Float east;

    @Enumerated(EnumType.STRING)
    private GeoPointStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Community community;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private LocalUser localUser;

}
