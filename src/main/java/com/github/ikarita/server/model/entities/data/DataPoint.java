package com.github.ikarita.server.model.entities.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.ikarita.server.model.entities.AbstractIkaritaEntity;
import com.github.ikarita.server.model.entities.community.Community;
import com.github.ikarita.server.model.entities.user.LocalUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ikarita_data_point")
public class DataPoint extends AbstractIkaritaEntity {
    @Enumerated(EnumType.STRING)
    private FeatureStatus status;

    private Point coordinates;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Community community;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private LocalUser localUser;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private JsonNode metadata;
}
