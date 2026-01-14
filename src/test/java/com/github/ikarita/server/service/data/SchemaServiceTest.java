package com.github.ikarita.server.service.data;

import com.github.ikarita.server.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.networknt.schema.Error;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

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
        final List<Error> errors = schemaService.validate(jsonContent);
        assertEquals(2, errors.size());
        assertTrue(errors.stream().anyMatch(e -> e.getMessage().equals("required property '$schema' not found")));
        assertTrue(errors.stream().anyMatch(e -> e.getMessage().equals("required property 'type' not found")));
    }

    @Test
    void testWrongSchema() throws JsonProcessingException {
        final String jsonContent = Utils.getFileContentFail("schema/wrong_schema.json");

        final var errors = schemaService.validate(jsonContent);
        assertEquals(1, errors.size());
        final var error = errors.getFirst();
        assertEquals("does not match the regex pattern https://json-schema.org/draft/2020-12/schema", error.getMessage());
        assertEquals("/$schema", error.getInstanceLocation().toString());
    }

    @Test
    void testRightSchema() throws JsonProcessingException {
        final String jsonContent = Utils.getFileContentFail("schema/right_schema.json");

        final List<Error> errors = schemaService.validate(jsonContent);
        assertEquals(0, errors.size());
    }

    @Test
    void testWrongId() throws JsonProcessingException {
        final String jsonContent = Utils.getFileContentFail("schema/wrong_id.json");

        final List<Error> errors = schemaService.validate(jsonContent);
        assertEquals(1, errors.size());
        final Error error = errors.getFirst();
        assertEquals("does not match the regex pattern ^(https?|http?)://", error.getMessage());
        assertEquals("/$id", error.getInstanceLocation().toString());
    }

    @Test
    void testRightId() throws JsonProcessingException {
        final var jsonContent = Utils.getFileContentFail("schema/right_id.json");

        final var errors = schemaService.validate(jsonContent);
        assertEquals(0, errors.size());
    }

    @Test
    void testWrongType() throws JsonProcessingException {
        final var jsonContent = Utils.getFileContentFail("schema/wrong_type.json");

        final var errors = schemaService.validate(jsonContent);
        assertEquals(1, errors.size());
        final var error = errors.getFirst();
        assertEquals("does not match the regex pattern object", error.getMessage());
        assertEquals("/type", error.getInstanceLocation().toString());
    }

    @Test
    void testSimpleSchema() throws JsonProcessingException {
        final var simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2020-12/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                        "boolean-property": {
                            "type" :"boolean"
                        }
                    }
                }""";

        final List<Error> errors = schemaService.validate(simpleSchema);
        assertEquals(0, errors.size());
    }

    @Test
    void testTwoBoolSchema() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2020-12/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                        "boolean-property": {
                            "type" :"boolean"
                        },
                       "boolean-property2": {
                            "type" :"boolean"
                        }
                    }
                }""";

        final List<Error> errors = schemaService.validate(simpleSchema);
        assertEquals(0, errors.size());
    }

    @Test
    void testWrongPropertySchema() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2020-12/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                        "boolean-property": {
                            "type" :"unknown"
                        }
                    }
                }""";

        final List<Error> errors = schemaService.validate(simpleSchema);
        assertFalse(errors.isEmpty());
    }

    @Test
    void testTwoIdenticalPropertiesSchema() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2020-12/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                        "boolean-property": {
                            "type" :"boolean"
                        },
                       "boolean-property": {
                            "type" :"boolean"
                        }
                    }
                }""";

        schemaService.validate(simpleSchema);
    }

    @Test
    void testNoPropertySchema() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2020-12/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                    }
                }""";

        final List<Error> errors = schemaService.validate(simpleSchema);
        assertEquals(0, errors.size());
    }

    @Test
    void testBooleanPassing() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2020-12/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                        "boolean-property": {
                            "type" :"boolean"
                        }
                    }
                }""";

        final String simpleObject = """
                {
                    "boolean-property": true
                }""";

        final List<Error> errors = schemaService.validate(simpleSchema, simpleObject);
        assertEquals(0, errors.size());
    }

    @Test
    void testBooleanFailing() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2020-12/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                        "boolean-property": {
                            "type" :"boolean"
                        }
                    }
                }""";

        final String simpleObject = """
                {
                    "boolean-property": "string"
                }""";

        final List<Error> errors = schemaService.validate(simpleSchema, simpleObject);
        assertEquals(1, errors.size());
        final var error = errors.getFirst();
        assertEquals("string found, boolean expected", error.getMessage());
        assertEquals("/boolean-property", error.getInstanceLocation().toString());
    }

    @Test
    void testBasicTypePropertiesSchema() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2020-12/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                        "boolean-property": {
                            "type" :"boolean"
                        },
                        "integer-property": {
                            "type" :"integer"
                        },
                        "number-property": {
                            "type" :"number"
                        },
                        "string-property": {
                            "type" :"string"
                        }
                    }
                }""";

        final List<Error> errors = schemaService.validate(simpleSchema);
        assertEquals(0, errors.size());
    }
}
