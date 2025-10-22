package com.andrem91.ProductComparisonAPI.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração OpenAPI/Swagger para documentação da API.
 * Acessível em /swagger-ui.html
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Product Comparison API",
        version = "1.0.0",
        description = """
            API RESTful para comparação inteligente de produtos com análise de categorias.
            """
    )
)
public class OpenApiConfig {
}
