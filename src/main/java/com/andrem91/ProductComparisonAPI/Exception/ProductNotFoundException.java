package com.andrem91.ProductComparisonAPI.Exception;

/** Exceção lançada quando produto não é encontrado. */
public class ProductNotFoundException extends BusinessException {
    
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Product not found with ID: %d";
    
    public ProductNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, id));
    }
    
    public ProductNotFoundException(String message) {
        super(message);
    }
}
