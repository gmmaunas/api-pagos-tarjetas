package com.dbd.service.pagos_tarjetas.conf;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Pagos con Tarjetas de Crédito API")
                        .version("1.0.0")
                        .description("API REST para la gestión de pagos con tarjetas de crédito. " +
                                "Este sistema permite administrar bancos, titulares, tarjetas, compras, " +
                                "promociones y generar pagos mensuales automáticamente.\n\n" +
                                "**Trabajo Final 2025 - DISEÑO DE BASES DE DATOS - MG IS 2025**")
                        .contact(new Contact()
                                .name("Gustavo Martín Maunás")
                                .email("maunasg@gmail.com"))
                        .license(new License()
                                .name("Proyecto Académico")
                                .url("https://github.com/gmmaunas/api-pagos-tarjetas")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor de Desarrollo")
                ));
    }
}
