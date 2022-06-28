package com.github.ikarita.server.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SchemaServiceImpl implements SchemaService{
    private final JsonSchema metaSchema;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Set<ValidationMessage> validate(String schema) throws JsonProcessingException {
        final JsonNode json = mapper.readTree(schema);
        return metaSchema.validate(json);
    }

    @Override
    public Set<ValidationMessage> validate( String schema,String object) throws JsonProcessingException {
        final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
        JsonSchema jsonSchema  = factory.getSchema(schema);
        final JsonNode json = mapper.readTree(object);
        return jsonSchema.validate(json);
    }

}
