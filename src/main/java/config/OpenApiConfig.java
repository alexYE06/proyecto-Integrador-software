package config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SAT-Carmen API - Monitoreo & Respuesta Rápida")
                        .version("1.0.0")
                        .description("Documentación interactiva de la API REST del Sistema de Alertas Carmen de la Punta S.A. "
                                + "Permite el monitoreo de unidades de transporte, despacho de auxilios, bitácora de incidentes y gestión de conductores.")
                        .contact(new Contact()
                                .name("Equipo de Sistemas - Carmen de la Punta")
                                .email("soporte@carmendelapunta.pe"))
                        .license(new License()
                                .name("Uso Académico / Proyecto Integrador")));
    }
}
