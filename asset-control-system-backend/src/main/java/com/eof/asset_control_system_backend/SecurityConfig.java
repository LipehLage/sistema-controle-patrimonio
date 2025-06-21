package com.eof.asset_control_system_backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:8082")); // Certifique-se que esta porta está correta
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Equipments
                                .requestMatchers(HttpMethod.POST, "/api/v1/equipments").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/equipments/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/equipments/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/equipments", "/api/v1/equipments/**").hasAnyRole("ADMIN", "USER")
                                // Users
                                .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users", "/api/v1/users/**").hasAnyRole("ADMIN", "USER")
                                // Movements
                                .requestMatchers(HttpMethod.POST, "/api/v1/movements/register").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/movements").hasAnyRole("ADMIN", "USER")
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || realmAccess.isEmpty()) {
                return java.util.Collections.emptyList();
            }
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            if (roles == null || roles.isEmpty()) {
                return java.util.Collections.emptyList();
            }
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return jwtConverter;
    }
}