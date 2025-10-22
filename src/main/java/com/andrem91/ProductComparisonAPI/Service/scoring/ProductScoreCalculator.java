package com.andrem91.ProductComparisonAPI.Service.scoring;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Calcula pontuação de produtos baseado em critérios definidos.
 */
@Component
public class ProductScoreCalculator {
    
    private static final int POINTS_PER_CATEGORY_WIN = 15;
    private static final int POINTS_HIGH_RATING = 10;
    private static final int POINTS_COMPETITIVE_PRICE = 5;
    private static final double HIGH_RATING_THRESHOLD = 4.7;
    private static final double COMPETITIVE_PRICE_THRESHOLD = 1000.0;
    
    /**
     * Calcula pontuação baseado em vitórias por categoria.
     * @param product Produto a avaliar
     * @param categoryWinners Vencedores por categoria
     * @return Pontuação total (0-100)
     */
    public int calculateScore(ProductEntity product, Map<String, ComparisonAnalysis.CategoryWinner> categoryWinners) {
        int score = 0;
        
        // +15 pontos por categoria vencida
        score += countCategoryWins(product, categoryWinners) * POINTS_PER_CATEGORY_WIN;
        
        // +10 pontos por rating alto (acima de 4.7)
        if (product.getRating() >= HIGH_RATING_THRESHOLD) {
            score += POINTS_HIGH_RATING;
        }
        
        // +5 pontos por preço competitivo (abaixo de $1000)
        if (product.getPrice() < COMPETITIVE_PRICE_THRESHOLD) {
            score += POINTS_COMPETITIVE_PRICE;
        }
        
        return score;
    }
    
    /**
     * Conta categorias vencidas pelo produto.
     */
    private int countCategoryWins(ProductEntity product, Map<String, ComparisonAnalysis.CategoryWinner> categoryWinners) {
        return (int) categoryWinners.values().stream()
                .filter(winner -> winner.getWinnerId().equals(product.getId()))
                .count();
    }
}
