package com.andrem91.ProductComparisonAPI.Service.analyzer;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;

import java.util.List;

/**
 * Interface para analisadores de categorias (Strategy Pattern).
 * Permite adicionar novas categorias sem modificar código existente.
 */
public interface CategoryAnalyzer {
    
    /** Retorna nome da categoria. */
    String getCategoryName();
    
    /**
     * Analisa produtos e identifica vencedor (Null Object Pattern).
     * @return CategoryWinner ou empty() se não aplicável
     */
    ComparisonAnalysis.CategoryWinner analyze(List<ProductEntity> products);
}
