package com.example.rentalaccommodation.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("1.6.8") String appVersion) {
        var securitySchemeName = "bearer-key";
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new io.swagger.v3.oas.models.security.SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info().title("Document API").version("1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
