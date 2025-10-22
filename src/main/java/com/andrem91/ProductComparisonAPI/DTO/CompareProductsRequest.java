package com.andrem91.ProductComparisonAPI.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para requisição de comparação de produtos.
 * Contém validações para garantir que a entrada seja válida.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para comparação de produtos")
public class CompareProductsRequest {
    
    @NotNull(message = "A lista de IDs não pode ser nula")
    @Size(min = 2, max = 5, message = "Deve fornecer entre 2 e 5 produtos para comparação")
    @Schema(
        description = "Lista de IDs de produtos para comparar",
        example = "[1, 2, 3]",
        required = true,
        minLength = 2,
        maxLength = 5
    )
    private List<Long> ids;
}
