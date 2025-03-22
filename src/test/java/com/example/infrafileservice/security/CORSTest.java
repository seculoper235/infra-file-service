package com.example.infrafileservice.security;

import com.example.infrafileservice.environment.WebIntegrationTest;
import com.example.infrafileservice.web.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
public class CORSTest extends WebIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Test
    @DisplayName("Simple Request은 Origin을 만족한다")
    void simple_request_satisfied_allow_origin_test() {
        assertDoesNotThrow(() -> {
            CorsConfiguration corsConfiguration = ((UrlBasedCorsConfigurationSource) corsConfigurationSource).getCorsConfigurations().get("/**");

            List<String> allowedOrigins = corsConfiguration.getAllowedOrigins();

            // Given
            assertNotNull(allowedOrigins);
            String origin = allowedOrigins.get(0);

            mockMvc.perform(
                            put("/logout")
                                    .header("Origin", origin)
                    )
                    .andDo(print())
                    .andExpect(header().stringValues("Access-Control-Allow-Origin", allowedOrigins.toArray(String[]::new)));
        });
    }

    @Test
    @DisplayName("Preflight Request는 Origin과 OPTIONS 메소드를 허용해야 한다")
    void preflight_request_satisfied_allow_origin_options_test() {
        assertDoesNotThrow(() -> {
            CorsConfiguration corsConfiguration = ((UrlBasedCorsConfigurationSource) corsConfigurationSource).getCorsConfigurations().get("/**");

            List<String> allowedOrigins = corsConfiguration.getAllowedOrigins();

            // Given
            assertNotNull(allowedOrigins);
            String origin = allowedOrigins.get(0);
            String method = "OPTIONS";

            mockMvc.perform(
                            options("/login")
                                    .header("Origin", origin)
                                    .header("Access-Control-Request-Method", "POST")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().stringValues("Access-Control-Allow-Origin", allowedOrigins.toArray(String[]::new)))
                    .andExpect(header().string("Access-Control-Allow-Methods", containsString(method)));
        });
    }
}
