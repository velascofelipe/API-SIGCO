package io.sigco.contactapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

// Importación de constantes relacionadas con las cabeceras HTTP

import static io.sigco.contactapi.constant.Constant.X_REQUESTED_WITH;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

// Configuración de Spring, indicando que esta clase es una configuración

@Configuration
public class CorsConfig {

// Método que crea y devuelve un filtro CORS configurado

    @Bean
    public CorsFilter corsFilter() {
        // Configuración de origen basado en URL para CORS
        var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        // Configuración específica de CORS
        var corsConfiguration = new CorsConfiguration();
        // Permitir el envío de cookies y credenciales
        corsConfiguration.setAllowCredentials(true);
        // Lista de orígenes permitidos
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4200"));
        // Lista de cabeceras permitidas
        corsConfiguration.setAllowedHeaders(List.of(ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT,
                AUTHORIZATION, X_REQUESTED_WITH, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS,
                ACCESS_CONTROL_ALLOW_CREDENTIALS));
        // Lista de cabeceras expuestas al cliente
        corsConfiguration.setExposedHeaders(List.of(ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT,
                AUTHORIZATION, X_REQUESTED_WITH, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS,
                ACCESS_CONTROL_ALLOW_CREDENTIALS));
        // Lista de métodos HTTP permitidos
        corsConfiguration.setAllowedMethods(List.of(GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(),
                OPTIONS.name()));
        // Configurar la URL de origen para la configuración CORS
        // "**" se aplica a todas las rutas
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        // Crear y devolver un filtro CORS configurado
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}




















