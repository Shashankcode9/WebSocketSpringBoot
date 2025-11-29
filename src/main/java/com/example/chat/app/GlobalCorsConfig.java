package com.example.chat.app;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures global CORS settings for the application.
 * This allows web clients from different origins to access the API.
 */
@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // This applies CORS configuration to all endpoints
                .allowedOriginPatterns("*") // Allows all origins. For production, you should restrict this to your frontend's domain.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specifies the allowed HTTP methods
                .allowedHeaders("*") // Allows all headers in the request
                .allowCredentials(true); // Allows cookies and credentials to be sent
    }
}
