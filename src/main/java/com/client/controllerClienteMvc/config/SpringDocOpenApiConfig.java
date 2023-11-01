package com.client.controllerClienteMvc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("security", securityScheme()))
                .info(
                        new Info()
                                .title("REST API - SPRING CONTROLLER CLIENT")
                                .description("API para controle de clientes, carros e serviços")
                                .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/"))
                                .contact(new Contact().email("carlosramos10k@gmail.com").url("https://www.linkedin.com/in/carlos-sousa-26b4b5212/"))
                );
    }

    private SecurityScheme securityScheme(){
        return new SecurityScheme()
                .description("Insira um BEARER token válidos pafra prosseguir")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("security");
    }
}
