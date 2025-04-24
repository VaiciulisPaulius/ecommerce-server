package lt.viko.eif.pvaiciulis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up web configurations in the application.
 */
@Configuration
public class WebConfig {

    /**
     * Configuration for development CORS.
     */
    @Configuration
    @Profile("dev")
    public static class DevCorsConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5174", "http://localhost:5173", "http://localhost:5175", "http://localhost:7175")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    }

    /**
     * Configuration for production CORS.
     */
    @Configuration
    @Profile("prod")
    public static class ProdCorsConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("https://your-production-domain.com") // replace with your frontend domain
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                    .allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "Accept")
                    .allowCredentials(true);
        }
    }

    /**
     * Shared configuration for static resources.
     */
    @Configuration
    public static class StaticResourceConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/uploads/images/**")
                    .addResourceLocations("file:uploads/images/")
                    .setCachePeriod(3600) // 1 hour caching
                    .resourceChain(true);
        }
    }
}
