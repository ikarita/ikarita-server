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
public class DataType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "parent")
    private List<DataType> dataTypes;

    @ManyToOne
    private DataType parent;

    @ManyToMany
    private List<DataConstraint> dataConstraints;


}
