package com.andrem91.ProductComparisonAPI.Exception;

/** Exceção para requisição de comparação inválida. */
public class InvalidComparisonRequestException extends BusinessException {
    
    public InvalidComparisonRequestException(String message) {
        super(message);
    }
    
    public InvalidComparisonRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
