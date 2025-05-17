package com.github.ikarita.server.service.data;

import com.github.ikarita.server.Utils;

import com.fasterxml.jackson.core.JsonParseException;
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
        assertTrue(errors.stream().anyMatch(e -> e.getMessage().equals("$: required property '$schema' not found")));
        assertTrue(errors.stream().anyMatch(e -> e.getMessage().equals("$: required property 'type' not found")));
    }

    @Test
    void testWrongSchema() throws JsonProcessingException {
        final String jsonContent = Utils.getFileContentFail("schema/wrong_schema.json");

        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(1, errors.size());
        assertTrue(errors.iterator().next().getMessage().startsWith("$.$schema: does not match the uri pattern"));

    }

    @Test
    void testRightSchema() throws JsonProcessingException {
        final String jsonContent = Utils.getFileContentFail("schema/right_schema.json");

        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(0, errors.size());
    }

    @Test
    void testWrongId() throws JsonProcessingException {
        final String jsonContent = Utils.getFileContentFail("schema/wrong_id.json");

        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(1, errors.size());
        assertTrue(errors.iterator().next().getMessage().startsWith("$.$id: does not match the uri pattern"));
    }

    @Test
    void testRightId() throws JsonProcessingException {
        final String jsonContent = Utils.getFileContentFail("schema/right_id.json");

        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(0, errors.size());
    }

    @Test
    void testWrongType() throws JsonProcessingException {
        final String jsonContent = Utils.getFileContentFail("schema/wrong_type.json");

        final Set<ValidationMessage> errors = schemaService.validate(jsonContent);
        assertEquals(1, errors.size());
        assertTrue(errors.iterator().next().getMessage().startsWith("$.type: must be a constant value object"));
    }

    @Test
    void testSimpleSchema() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2019-09/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                        "boolean-property": {
                            "type" :"boolean"
                        }
                    }
                }""";

        final Set<ValidationMessage> errors = schemaService.validate(simpleSchema);
        assertEquals(0, errors.size());
    }

    @Test
    void testTwoBoolSchema() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2019-09/schema",
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

        final Set<ValidationMessage> errors = schemaService.validate(simpleSchema);
        assertEquals(0, errors.size());
    }

    @Test
    void testTwoIdenticalPropertiesSchema() {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2019-09/schema",
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

        assertThrows(JsonParseException.class, () -> schemaService.validate(simpleSchema));
    }

    @Test
    void testNoPropertySchema() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2019-09/schema",
                    "$id": "https://ikarita.org/shemas/001",
                    "type": "object",
                    "properties": {
                    }
                }""";

        final Set<ValidationMessage> errors = schemaService.validate(simpleSchema);
        assertEquals(0, errors.size());
    }

    @Test
    void testBooleanPassing() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2019-09/schema",
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

        final Set<ValidationMessage> errors = schemaService.validate(simpleSchema, simpleObject);
        assertEquals(0, errors.size());
    }

    @Test
    void testBooleanFailing() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2019-09/schema",
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

        final Set<ValidationMessage> errors = schemaService.validate(simpleSchema, simpleObject);
        assertEquals(1, errors.size());
        assertEquals("$.boolean-property: string found, boolean expected", errors.iterator().next().getMessage());
    }

    @Test
    void testBasicTypePropertiesSchema() throws JsonProcessingException {
        final String simpleSchema = """
                {
                    "$schema": "https://json-schema.org/draft/2019-09/schema",
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

        final Set<ValidationMessage> errors = schemaService.validate(simpleSchema);
        assertEquals(0, errors.size());

    }
}
