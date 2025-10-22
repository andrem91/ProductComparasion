package com.andrem91.ProductComparisonAPI.Service;

import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import com.andrem91.ProductComparisonAPI.Exception.InvalidComparisonRequestException;
import com.andrem91.ProductComparisonAPI.Repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Gerencia lógica de negócio de produtos.
 * Responsável por recuperação e validação de dados.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final IProductRepository productRepository;
    
    /**
     * Recupera todos os produtos.
     */
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
    
    /**
     * Recupera produto por ID.
     */
    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    /**
     * Recupera produtos para comparação (2-5 produtos).
     * @param ids Lista de IDs
     * @return Lista de produtos
     * @throws InvalidComparisonRequestException se validação falhar
     */
    public List<ProductEntity> getProductsForComparison(List<Long> ids) {
        // Mínimo 2 produtos
        if (ids == null || ids.size() < 2) {
            throw new InvalidComparisonRequestException("At least 2 product IDs are required for comparison");
        }
        
        // Máximo 5 produtos
        if (ids.size() > 5) {
            throw new InvalidComparisonRequestException("Cannot compare more than 5 products at once");
        }
        
        // Busca produtos
        List<ProductEntity> products = productRepository.findByIds(ids);
        
        // Valida se todos foram encontrados
        if (products.size() != ids.size()) {
            throw new InvalidComparisonRequestException("One or more product IDs not found");
        }
        
        return products;
    }
}
