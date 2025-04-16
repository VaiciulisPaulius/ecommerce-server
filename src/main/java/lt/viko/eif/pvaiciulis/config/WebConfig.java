package lt.viko.eif.pvaiciulis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up web configurations in the application.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Configures CORS mapping for the application.
     *
     * @param registry the CorsRegistry to configure.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5174", "http://localhost:5173", "http://localhost:5175", "http://localhost:7175")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
