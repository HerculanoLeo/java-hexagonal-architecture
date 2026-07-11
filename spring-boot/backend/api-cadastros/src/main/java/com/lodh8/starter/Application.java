package com.lodh8.starter;

import com.herculanoleo.spring.me.models.annotation.EnableFeignMapperEnum;
import com.herculanoleo.spring.me.models.annotation.EnableMapperEnum;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.modulith.Modulith;

@EnableMapperEnum
@EnableFeignMapperEnum
@EnableFeignClients
@OpenAPIDefinition(
        info = @Info(title = "API - Cadastros", version = "1.0"),
        security = {@SecurityRequirement(name = "OAuth2")},
        servers = {@Server(url = "/", description = "Default Server URL")}
)
@Modulith(systemName = "API - Cadastros")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
