package com.github.ikarita.server.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.networknt.schema.InputFormat;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.dialect.Dialects;
import com.networknt.schema.serialization.DefaultNodeReader;
import org.springframework.stereotype.Service;
import com.networknt.schema.Error;

import java.util.List;

@Service
public class SchemaServiceImpl implements SchemaService{
    private final Schema metaSchema;

    public SchemaServiceImpl(Schema metaSchema) {
        this.metaSchema = metaSchema;
    }

    @Override
    public List<Error> validate(String jsonSchema) throws JsonProcessingException {
        return metaSchema.validate(jsonSchema, InputFormat.JSON);
    }

    @Override
    public List<Error> validate(String jsonSchema, String object) throws JsonProcessingException {
        final SchemaRegistry schemaRegistry = SchemaRegistry.withDialect(Dialects.getDraft202012(),
                builder -> builder.nodeReader(DefaultNodeReader.Builder::locationAware));
        final Schema schema = schemaRegistry.getSchema(jsonSchema, InputFormat.JSON);
        return schema.validate(object, InputFormat.JSON);
    }

}
