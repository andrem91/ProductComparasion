package com.andrem91.ProductComparisonAPI.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respostas de erro padronizadas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta de erro padronizada da API")
public class ErrorResponse {
    
    @Schema(description = "Timestamp do erro", example = "2025-10-20T16:14:39")
    private LocalDateTime timestamp;
    
    @Schema(description = "Código de status HTTP", example = "400")
    private int status;
    
    @Schema(description = "Tipo do erro", example = "Bad Request")
    private String error;
    
    @Schema(description = "Mensagem principal do erro", example = "Validação falhou")
    private String message;
    
    @Schema(description = "Caminho da requisição que gerou o erro", example = "/api/products/compare")
    private String path;
    
    @Schema(description = "Lista de erros de validação detalhados")
    private List<ValidationError> validationErrors;
    
    /** Erro de validação individual. */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Erro de validação individual")
    public static class ValidationError {
        
        @Schema(description = "Campo que falhou na validação", example = "ids")
        private String field;
        
        @Schema(description = "Mensagem de erro", example = "Deve fornecer entre 2 e 5 produtos")
        private String message;
        
        @Schema(description = "Valor rejeitado", example = "[1]")
        private Object rejectedValue;
    }
}
