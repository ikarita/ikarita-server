package com.github.ikarita.server.model.entities.data;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ikarita_data_type")
public class MetadataType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "parent")
    private List<MetadataType> metadataTypes;

    @ManyToOne
    private MetadataType parent;

    @ManyToMany
    private List<MetadataConstraint> metadataConstraints;


}
