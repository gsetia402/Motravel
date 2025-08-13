package org.moto.motravel.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Motravel API",
        version = "1.0",
        description = "Vehicle Rental Application API - Use /api/auth/signin to get JWT token for protected endpoints",
        contact = @Contact(
            name = "Motravel Support",
            email = "support@motravel.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Development Server"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT token obtained from /api/auth/signin endpoint"
)
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Motravel API")
                        .version("1.0")
                        .description("Vehicle Rental Application API\n\n" +
                                "**Authentication Instructions:**\n" +
                                "1. Use `/api/auth/signin` endpoint to get JWT token\n" +
                                "2. Click 'Authorize' button and enter: `Bearer <your-jwt-token>`\n" +
                                "3. Now you can access protected endpoints\n\n" +
                                "**Public Endpoints (No Auth Required):**\n" +
                                "- GET /api/vehicles (Browse all vehicles)\n" +
                                "- GET /api/vehicles/{id} (View vehicle details)\n" +
                                "- GET /api/vehicles/available (View available vehicles)\n" +
                                "- GET /api/vehicles/nearby (Find nearby vehicles)\n" +
                                "- POST /api/auth/signin (Login)\n" +
                                "- POST /api/auth/signup (Register)\n\n" +
                                "**Protected Endpoints (Auth Required):**\n" +
                                "- All booking endpoints\n" +
                                "- Vehicle management (POST, PUT, DELETE)\n"));
    }
}
