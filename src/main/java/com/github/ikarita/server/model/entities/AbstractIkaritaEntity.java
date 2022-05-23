package com.github.ikarita.server.model.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class AbstractIkaritaEntity {
    @Id
    @Type(type = "pg-uuid")
    private UUID id;

    public AbstractIkaritaEntity() {
        this.id = UUID.randomUUID();
    }
}
