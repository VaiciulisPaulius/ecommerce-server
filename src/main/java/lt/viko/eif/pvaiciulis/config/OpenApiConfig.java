package lt.viko.eif.pvaiciulis.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Configuration class for OpenAPI documentation.
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Paulius Vaiciulis",
                        email = "paulius.vaiciulis@stud.viko.lt",
                        url = "eif.viko.lt"
                ),
                description = "OpenApi documentation for e-commerce application",
                title = "e-commerce application",
                version = "1.0",
                license = @License(
                        name = "Free to use",
                        url = "eif.viko.lt"
                )
        ),
        servers = {
                @Server(
                        description = "Local DEV",
                        url = "http://localhost:7175"
                ),
                @Server(
                        description = "Test server",
                        url = "http://localhost:7175"
                )

        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
