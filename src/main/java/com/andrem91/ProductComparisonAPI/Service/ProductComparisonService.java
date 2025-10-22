package com.andrem91.ProductComparisonAPI.Service;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Orquestra busca e análise de produtos (Facade Pattern).
 * Simplifica interface do controller encapsulando operações múltiplas.
 */
@Service
@RequiredArgsConstructor
public class ProductComparisonService {
    
    private final ProductService productService;
    private final ComparisonAnalysisService analysisService;
    
    /**
     * Busca e analisa produtos em uma operação.
     * @param productIds IDs dos produtos (2-5)
     * @return Análise completa com vencedores por categoria
     */
    public ComparisonAnalysis compareAndAnalyze(List<Long> productIds) {
        // Busca produtos
        List<ProductEntity> products = productService.getProductsForComparison(productIds);
        
        // Analisa
        return analysisService.analyzeProducts(products);
    }
    
    /**
     * Analisa produtos já carregados.
     */
    public ComparisonAnalysis analyzeProducts(List<ProductEntity> products) {
        return analysisService.analyzeProducts(products);
    }
}
