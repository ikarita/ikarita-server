package com.github.ikarita.server.service.data;

import com.networknt.schema.InputFormat;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.dialect.Dialects;
import com.networknt.schema.serialization.DefaultNodeReader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Configuration
public class SchemaConfiguration {
    private final ResourceLoader resourceLoader;

    @Bean
    Schema metaSchema() throws IOException {
        final Resource resource = resourceLoader.getResource("classpath:meta-schema.json");
        try(InputStream resourceAsStream = resource.getInputStream()){
            final SchemaRegistry schemaRegistry = SchemaRegistry.withDialect(Dialects.getDraft202012(),
                    builder -> builder.nodeReader(DefaultNodeReader.Builder::locationAware));

            return schemaRegistry.getSchema(resourceAsStream, InputFormat.JSON);
        }
    }
}
