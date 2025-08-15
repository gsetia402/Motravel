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
                        .description("Motravel - Vehicle Rental & Hidden Gems Travel Platform API\n\n" +
                                "**Authentication Instructions:**\n" +
                                "1. Use `/api/auth/signin` endpoint to get JWT token\n" +
                                "2. Click 'Authorize' button and enter: `Bearer <your-jwt-token>`\n" +
                                "3. Now you can access protected endpoints\n\n" +
                                "**🚗 Vehicle Rental - Public Endpoints:**\n" +
                                "- GET /api/vehicles (Browse all vehicles)\n" +
                                "- GET /api/vehicles/{id} (View vehicle details)\n" +
                                "- GET /api/vehicles/available (View available vehicles)\n" +
                                "- GET /api/vehicles/nearby (Find nearby vehicles)\n\n" +
                                "**🏔️ Hidden Gems - Public Endpoints:**\n" +
                                "- GET /api/hidden-gems (Browse hidden travel destinations with filtering)\n" +
                                "- GET /api/hidden-gems/{id} (View hidden gem details)\n" +
                                "- GET /api/hidden-gems/nearby (Find nearby hidden gems)\n" +
                                "- GET /api/states (List all states)\n" +
                                "- GET /api/adventure-types (List all adventure types)\n\n" +
                                "**🔐 Authentication Endpoints:**\n" +
                                "- POST /api/auth/signin (Login)\n" +
                                "- POST /api/auth/signup (Register)\n\n" +
                                "**🔒 Protected User Endpoints (Auth Required):**\n" +
                                "- All booking endpoints\n" +
                                "- POST/DELETE /api/hidden-gems/{id}/bookmark (Bookmark hidden gems)\n" +
                                "- GET /api/users/bookmarks (View user's bookmarked gems)\n\n" +
                                "**👑 Admin Endpoints (Admin Role Required):**\n" +
                                "- Vehicle management (POST, PUT, DELETE /api/vehicles)\n" +
                                "- Hidden gems management (/api/admin/hidden-gems)\n" +
                                "- States management (/api/admin/states)\n" +
                                "- Adventure types management (/api/admin/adventure-types)\n"));
    }
}
