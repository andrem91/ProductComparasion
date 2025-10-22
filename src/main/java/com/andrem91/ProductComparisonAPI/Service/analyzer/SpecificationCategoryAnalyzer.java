package com.andrem91.ProductComparisonAPI.Service.analyzer;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;

import java.util.List;

/**
 * Analisador base para especificações numéricas (Template Method Pattern).
 * Subclasses apenas configuram parâmetros no construtor.
 */
public abstract class SpecificationCategoryAnalyzer implements CategoryAnalyzer {
    
    private final String categoryName;
    private final String specKey;
    private final String description;
    private final boolean higherIsBetter;
    
    /** Configura analisador de especificação. */
    protected SpecificationCategoryAnalyzer(String categoryName, String specKey, String description, boolean higherIsBetter) {
        this.categoryName = categoryName;
        this.specKey = specKey;
        this.description = description;
        this.higherIsBetter = higherIsBetter;
    }
    
    @Override
    public String getCategoryName() {
        return categoryName;
    }
    
    /** Implementa algoritmo comum (Template Method). */
    @Override
    public ComparisonAnalysis.CategoryWinner analyze(List<ProductEntity> products) {
        return products.stream()
                .filter(p -> p.getSpecifications().containsKey(specKey))
                .max((p1, p2) -> {
                    int value1 = extractNumericValue(p1.getSpecifications().get(specKey));
                    int value2 = extractNumericValue(p2.getSpecifications().get(specKey));
                    return higherIsBetter ? Integer.compare(value1, value2) : Integer.compare(value2, value1);
                })
                .map(product -> ComparisonAnalysis.CategoryWinner.builder()
                        .category(categoryName)
                        .winnerId(product.getId())
                        .winnerName(product.getName())
                        .value(product.getSpecifications().get(specKey))
                        .reason(description)
                        .build())
                .orElse(ComparisonAnalysis.CategoryWinner.empty(categoryName));
    }
    
    /** Extrai valor numérico de string (ex: "12GB" → 12). */
    protected int extractNumericValue(String value) {
        if (value == null) return 0;
        
        // Remove tudo exceto números e ponto decimal
        String numericPart = value.replaceAll("[^0-9.]", "");
        
        try {
            return (int) Double.parseDouble(numericPart);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
