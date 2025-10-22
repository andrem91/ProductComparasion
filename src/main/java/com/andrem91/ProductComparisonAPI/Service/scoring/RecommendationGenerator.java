package com.andrem91.ProductComparisonAPI.Service.scoring;

import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Gera texto de recomendação personalizado baseado em análise.
 */
@Component
public class RecommendationGenerator {
    
    private static final double HIGH_RATING_THRESHOLD = 4.7;
    private static final double COMPETITIVE_PRICE_THRESHOLD = 1000.0;
    
    /** Gera texto de recomendação humanizado. */
    public String generateRecommendation(ProductEntity product, List<String> strengths, List<String> weaknesses) {
        StringBuilder recommendation = new StringBuilder();
        
        recommendation.append(String.format("O %s é a melhor escolha geral nesta comparação. ", product.getName()));
        
        if (!strengths.isEmpty()) {
            recommendation.append(String.format("Destaca-se em %d categoria(s), ", strengths.size()));
            recommendation.append("oferecendo excelente desempenho onde mais importa. ");
        }
        
        if (product.getRating() >= HIGH_RATING_THRESHOLD) {
            recommendation.append("Possui avaliação excelente dos usuários. ");
        }
        
        if (product.getPrice() < COMPETITIVE_PRICE_THRESHOLD) {
            recommendation.append("Além disso, oferece ótimo custo-benefício. ");
        }
        
        if (!weaknesses.isEmpty()) {
            recommendation.append(String.format("Considere que possui %d ponto(s) de atenção. ", weaknesses.size()));
        }
        
        return recommendation.toString().trim();
    }
}
