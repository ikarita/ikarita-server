package com.github.ikarita.server.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ikarita.server.api.community.CommunityController;
import com.github.ikarita.server.model.dto.community.CommunityDto;
import com.github.ikarita.server.model.dto.community.NewCommunityDto;
import com.github.ikarita.server.service.community.CommunityService;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CommunityController.class)
class CommunityControllerTest extends AbstractControllerTest {
    @MockBean
    private CommunityService communityService;

    @Test
    void testGetCommunitiesWithoutAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/communities")).andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCommunityWithoutAuthenticationIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/communities/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCommunitiesWithAuthenticationIsOk() throws Exception {
        final CommunityDto community1 = new CommunityDto(1L, "Community 1", true);
        final CommunityDto community2 = new CommunityDto(2L, "Community 2", true);

        Mockito.when(communityService.getCommunities()).thenReturn(Arrays.asList(community1, community2));
        performAuthenticated(get("/api/v1/communities"), admin())
                .andExpect(status().isOk());
    }

    @Test
    void testPostCommunityWithViewerIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        performAuthenticated(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)), viewer())
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostCommunityWithModeratorIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        performAuthenticated(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)), moderator())
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostCommunityWithContributorIsForbidden() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);

        performAuthenticated(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)), contributor())
                .andExpect(status().isCreated());
    }

    @Test
    void testPostCommunityWithAuthenticationIsOk() throws Exception {
        final NewCommunityDto newCommunityDto = new NewCommunityDto("Community 1", true);
        final CommunityDto communityDto = new CommunityDto(1L, "Community 1", true);

        Mockito.when(communityService.createCommunity(any())).thenReturn(communityDto);
        performAuthenticated(post("/api/v1/communities").contentType(MediaType.APPLICATION_JSON).content(toJson(newCommunityDto)), admin())
                .andExpect(status().isCreated());
    }

    @Test
    void test() throws Exception {
        final String schemaContent = "{\n" +
                "  \"$schema\": \"https://json-schema.org/draft/2019-09/schema\",\n" +
                "  \"title\": \"Product\",\n" +
                "  \"description\": \"A product from Acme's catalog\",\n" +
                "  \"type\": \"object\",\n" +
                "  \"properties\": {\n" +
                "    \"productId\": {\n" +
                "      \"description\": \"The unique identifier for a product\",\n" +
                "      \"type\": \"integer\"\n" +
                "    },\n" +
                "    \"productName\": {\n" +
                "      \"description\": \"Name of the product\",\n" +
                "      \"type\": \"string\"\n" +
                "    },\n" +
                "    \"price\": {\n" +
                "      \"description\": \"The price of the product\",\n" +
                "      \"type\": \"number\",\n" +
                "      \"exclusiveMinimum\": 0\n" +
                "    },\n" +
                "    \"tags\": {\n" +
                "      \"description\": \"Tags for the product\",\n" +
                "      \"type\": \"array\",\n" +
                "      \"items\": {\n" +
                "        \"type\": \"string\"\n" +
                "      },\n" +
                "      \"minItems\": 1,\n" +
                "      \"uniqueItems\": true\n" +
                "    },\n" +
                "    \"dimensions\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"length\": {\n" +
                "          \"type\": \"number\"\n" +
                "        },\n" +
                "        \"width\": {\n" +
                "          \"type\": \"number\"\n" +
                "        },\n" +
                "        \"height\": {\n" +
                "          \"type\": \"number\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"required\": [ \"length\", \"width\", \"height\" ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"required\": [ \"productId\", \"productName\", \"price\" ],\n" +
                "  \"additionalProperties\": false" +
                "}";

        final String jsonContent = " {\n" +
                "    \"productId\": 1,\n" +
                "    \"productName\": \"An ice sculpture\",\n" +
                "    \"price\": -12.50,\n" +
                "    \"tags\": [ \"cold\", \"ice\" ],\n" +
                "    \"dimensions\": {\n" +
                "      \"length\": 7.0,\n" +
                "      \"width\": 12.0,\n" +
                "      \"height\": 9.5\n" +
                "    },\n" +
                "    \"warehouseLocation\": {\n" +
                "      \"latitude\": -278.75,\n" +
                "      \"longitude\": 20.4\n" +
                "    }\n" +
                "  }";

        final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
        JsonSchema schema = factory.getSchema(schemaContent);

        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode json = mapper.readTree(jsonContent);

        Set<ValidationMessage> errors = schema.validate(json);

        assertNotNull(errors);
    }
}