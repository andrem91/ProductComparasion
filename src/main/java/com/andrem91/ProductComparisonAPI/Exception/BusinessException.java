package com.andrem91.ProductComparisonAPI.Exception;

/**
 * Exceção base para erros de negócio da aplicação.
 * Segue o Liskov Substitution Principle (LSP) - pode substituir RuntimeException.
 * Facilita tratamento consistente de exceções de negócio.
 */
public abstract class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
