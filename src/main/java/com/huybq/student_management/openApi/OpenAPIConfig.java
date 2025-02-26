package com.huybq.student_management.openApi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Student management",
                version = "1.0",
                description = "API student management",
                contact = @Contact(name = "huybq", email = "huybq@gmail.com"),
                license = @License(name = "huybq", url = "huybq.dev.vn")

        )
)
public class OpenAPIConfig {
}
