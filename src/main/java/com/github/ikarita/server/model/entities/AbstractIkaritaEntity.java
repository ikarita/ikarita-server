package com.github.ikarita.server.model.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractIkaritaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
}
