package com.github.ikarita.server.service.data;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Configuration
public class SchemaConfiguration {
    private final ApplicationContext context;

    @Bean
    JsonSchema metaSchema() throws IOException {
        final Resource resource = context.getResource("meta-schema.json");
        try(InputStream resourceAsStream = resource.getInputStream()){
            final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
            return factory.getSchema(resourceAsStream);
        }
    }
}
