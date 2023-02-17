package com.tbook.rewardsphere

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(servers = [Server(url = "/", description = "Default Server URL")])
@Configuration
class SpringDocConfig {
    @Bean
    fun api(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("tbook")
            .pathsToMatch("/**")
            .build();
    }
}
