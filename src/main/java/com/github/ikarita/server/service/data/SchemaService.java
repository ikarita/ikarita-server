package com.github.ikarita.server.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.networknt.schema.ValidationMessage;

import java.util.Set;

public interface SchemaService {
    Set<ValidationMessage> validate(String schema) throws JsonProcessingException;

    Set<ValidationMessage> validate(String schema,String object) throws  JsonProcessingException;
}
