package org.moto.motravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Motravel API",
        version = "1.0",
        description = "Vehicle Rental Application API"
    )
)
public class MotravelApplication {
    public static void main(String[] args) {
        SpringApplication.run(MotravelApplication.class, args);
    }
}
