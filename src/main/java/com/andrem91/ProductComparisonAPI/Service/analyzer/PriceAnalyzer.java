package com.andrem91.ProductComparisonAPI.Service.analyzer;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Identifica produto com menor preço.
 */
@Component
public class PriceAnalyzer implements CategoryAnalyzer {
    
    @Override
    public String getCategoryName() {
        return "Melhor Preço";
    }
    
    @Override
    public ComparisonAnalysis.CategoryWinner analyze(List<ProductEntity> products) {
        return products.stream()
                .min(Comparator.comparing(ProductEntity::getPrice))
                .map(product -> ComparisonAnalysis.CategoryWinner.builder()
                        .category(getCategoryName())
                        .winnerId(product.getId())
                        .winnerName(product.getName())
                        .value(String.format("$%.2f", product.getPrice()))
                        .reason("Menor preço entre os produtos comparados")
                        .build())
                .orElse(ComparisonAnalysis.CategoryWinner.empty(getCategoryName()));
    }
}
