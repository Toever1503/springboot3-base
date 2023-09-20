package com.springboot3base.common.config;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI swaggerApi() {
        Server server = new Server()
                .url("/")
                .description("Default Server url");

        Info info = new Info()
                .version("v1.0.0")
                .title("Backend API Documentation")
                .description("Backend API")
                .license(new License().name("CY Vietnam").url("https://cyvietnam.com/"));

        Components components = new Components().addSecuritySchemes("x-api-token", new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("x-api-token"));

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("x-api-token");

        SpringDocUtils.getConfig().replaceWithClass(Pageable.class, Page.class);

        return new OpenAPI()
                .addServersItem(server)
                .info(info)
                .components(components)
                .security(List.of(securityRequirement));
    }

    @Getter
    @Setter
    @Schema
    static class Page {
        @Schema(description = "Page number start from 0", example = "0")
        private Integer page;

        @Schema(description = "Page size", example = "20")
        private Integer size;

        @Schema(description = "sort by field and direction, asc|desc)", example = "id,desc")
        private List<String> sort;
    }
}
