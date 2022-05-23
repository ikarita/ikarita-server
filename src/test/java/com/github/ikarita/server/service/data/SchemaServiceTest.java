package com.github.ikarita.server.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.networknt.schema.ValidationMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(SchemaServiceImpl.class)
@ContextConfiguration(classes = {SchemaConfiguration.class})
class SchemaServiceTest {
    @Autowired
    SchemaService schemaService;

    @Test
    void testEmpty() throws JsonProcessingException {
        final String jsonContent = "{}";
        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(2, errors.size());
        assertTrue(errors.stream().anyMatch(e -> e.getMessage().equals("$.$schema: is missing but it is required")));
        assertTrue(errors.stream().anyMatch(e -> e.getMessage().equals("$.type: is missing but it is required")));
    }

    @Test
    void testWrongSchema() throws JsonProcessingException {
        final String jsonContent = "{\n" +
                "    \"$schema\": \"schema.json\",\n" +
                "    \"type\": \"object\"\n" +
                "}";

        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(1, errors.size());
        assertTrue(errors.iterator().next().getMessage().startsWith("$.$schema: does not match the uri pattern"));
    }

    @Test
    void testRightSchema() throws JsonProcessingException {
        final String jsonContent = "{\n" +
                "    \"$schema\": \"https://json-schema.org/draft/2019-09/schema\",\n" +
                "    \"type\": \"object\"\n" +
                "}";

        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(0, errors.size());
    }

    @Test
    void testWrongId() throws JsonProcessingException {
        final String jsonContent = "{\n" +
                "    \"$schema\": \"https://json-schema.org/draft/2019-09/schema\",\n" +
                "    \"$id\": \"test1\",\n" +
                "    \"type\": \"object\"\n" +
                "}";

        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(1, errors.size());
        assertTrue(errors.iterator().next().getMessage().startsWith("$.$id: does not match the uri pattern"));
    }

    @Test
    void testRightId() throws JsonProcessingException {
        final String jsonContent = "{\n" +
                "    \"$schema\": \"https://json-schema.org/draft/2019-09/schema\",\n" +
                "    \"$id\": \"https://ikarita.org/shemas/001\",\n" +
                "    \"type\": \"object\"\n" +
                "}";

        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(0, errors.size());
    }
}
