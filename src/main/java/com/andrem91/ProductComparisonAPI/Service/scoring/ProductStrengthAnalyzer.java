package com.andrem91.ProductComparisonAPI.Service.scoring;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Identifica pontos fortes baseado em vitórias de categoria.
 */
@Component
public class ProductStrengthAnalyzer {
    
    /** Identifica pontos fortes do produto. */
    public List<String> identifyStrengths(ProductEntity product, Map<String, ComparisonAnalysis.CategoryWinner> categoryWinners) {
        List<String> strengths = new ArrayList<>();
        
        for (Map.Entry<String, ComparisonAnalysis.CategoryWinner> entry : categoryWinners.entrySet()) {
            if (entry.getValue().getWinnerId().equals(product.getId())) {
                strengths.add(entry.getKey() + ": " + entry.getValue().getValue());
            }
        }
        
        return strengths;
    }
}
