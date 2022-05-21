package com.github.ikarita.server.model.entities.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ikarita_data_constraint")
public class MetadataConstraint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DataValidationOperation dataValidationOperation;

    @ElementCollection
    @CollectionTable(name="ikarita_data_operation_argument", joinColumns=@JoinColumn(name="data_constraint_id"))
    @Column(name="argument")
    private List<String> arguments;
}
