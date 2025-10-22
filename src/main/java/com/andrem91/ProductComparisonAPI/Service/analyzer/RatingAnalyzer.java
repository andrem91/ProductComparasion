package com.andrem91.ProductComparisonAPI.Service.analyzer;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/** Identifica produto com maior avaliação. */
@Component
public class RatingAnalyzer implements CategoryAnalyzer {
    
    @Override
    public String getCategoryName() {
        return "Melhor Avaliação";
    }
    
    @Override
    public ComparisonAnalysis.CategoryWinner analyze(List<ProductEntity> products) {
        return products.stream()
                .max(Comparator.comparing(ProductEntity::getRating))
                .map(product -> ComparisonAnalysis.CategoryWinner.builder()
                        .category(getCategoryName())
                        .winnerId(product.getId())
                        .winnerName(product.getName())
                        .value(String.format("%.1f★", product.getRating()))
                        .reason("Maior avaliação dos usuários")
                        .build())
                .orElse(ComparisonAnalysis.CategoryWinner.empty(getCategoryName()));
    }
}
