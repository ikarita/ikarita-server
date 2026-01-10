package com.github.ikarita.server.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import com.networknt.schema.Error;


public interface SchemaService {
    List<Error> validate(String schema) throws JsonProcessingException;

    List<Error> validate(String schema, String object) throws  JsonProcessingException;
}
