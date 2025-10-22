package com.andrem91.ProductComparisonAPI.Service;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import com.andrem91.ProductComparisonAPI.Service.analyzer.CategoryAnalyzer;
import com.andrem91.ProductComparisonAPI.Service.scoring.ProductScoreCalculator;
import com.andrem91.ProductComparisonAPI.Service.scoring.ProductStrengthAnalyzer;
import com.andrem91.ProductComparisonAPI.Service.scoring.ProductWeaknessAnalyzer;
import com.andrem91.ProductComparisonAPI.Service.scoring.RecommendationGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Analisa e compara produtos identificando o melhor em cada categoria.
 * Usa Strategy Pattern para análise extensível de categorias.
 */
@Service
@RequiredArgsConstructor
public class ComparisonAnalysisService {
    
    private final List<CategoryAnalyzer> categoryAnalyzers;
    private final ProductScoreCalculator scoreCalculator;
    private final ProductStrengthAnalyzer strengthAnalyzer;
    private final ProductWeaknessAnalyzer weaknessAnalyzer;
    private final RecommendationGenerator recommendationGenerator;
    
    /**
     * Analisa produtos e retorna comparação detalhada.
     */
    public ComparisonAnalysis analyzeProducts(List<ProductEntity> products) {
        ComparisonAnalysis analysis = new ComparisonAnalysis();
        
        // Cria resumos
        List<ComparisonAnalysis.ProductSummary> summaries = createProductSummaries(products);
        analysis.setProducts(summaries);
        
        // Analisa categorias
        Map<String, ComparisonAnalysis.CategoryWinner> categoryWinners = analyzeCategoryWinners(products);
        analysis.setCategoryWinners(categoryWinners);
        
        // Calcula pontuação e recomendação
        ComparisonAnalysis.OverallRecommendation recommendation = calculateOverallRecommendation(products, categoryWinners);
        analysis.setOverallRecommendation(recommendation);
        
        return analysis;
    }
    
    /**
     * Cria resumos dos produtos (Factory Method Pattern).
     */
    private List<ComparisonAnalysis.ProductSummary> createProductSummaries(List<ProductEntity> products) {
        return products.stream()
                .map(ComparisonAnalysis.ProductSummary::from)
                .collect(Collectors.toList());
    }
    
    /**
     * Identifica melhor produto por categoria (Strategy Pattern).
     */
    private Map<String, ComparisonAnalysis.CategoryWinner> analyzeCategoryWinners(List<ProductEntity> products) {
        Map<String, ComparisonAnalysis.CategoryWinner> winners = new LinkedHashMap<>();
        
        // Itera analisadores
        for (CategoryAnalyzer analyzer : categoryAnalyzers) {
            ComparisonAnalysis.CategoryWinner winner = analyzer.analyze(products);
            if (winner.isPresent()) {
                winners.put(analyzer.getCategoryName(), winner);
            }
        }
        
        return winners;
    }
    
    /**
     * Calcula pontuação geral e gera recomendação final.
     */
    private ComparisonAnalysis.OverallRecommendation calculateOverallRecommendation(
            List<ProductEntity> products,
            Map<String, ComparisonAnalysis.CategoryWinner> categoryWinners) {
        
        // Calcula pontuações
        Map<Long, Integer> scores = new HashMap<>();
        for (ProductEntity product : products) {
            int score = scoreCalculator.calculateScore(product, categoryWinners);
            scores.put(product.getId(), score);
        }
        
        // Encontra o produto com maior pontuação
        Long bestProductId = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        
        ProductEntity bestProduct = products.stream()
                .filter(p -> p.getId().equals(bestProductId))
                .findFirst()
                .orElse(null);
        
        if (bestProduct == null) {
            return null;
        }
        
        // Identifica pontos fortes e fracos
        List<String> strengths = strengthAnalyzer.identifyStrengths(bestProduct, categoryWinners);
        List<String> weaknesses = weaknessAnalyzer.identifyWeaknesses(bestProduct, products);
        
        // Gera recomendação
        String recommendation = recommendationGenerator.generateRecommendation(bestProduct, strengths, weaknesses);
        
        // Builder Pattern
        return ComparisonAnalysis.OverallRecommendation.builder()
                .productId(bestProduct.getId())
                .productName(bestProduct.getName())
                .totalScore(scores.get(bestProductId))
                .recommendation(recommendation)
                .strengths(strengths)
                .weaknesses(weaknesses)
                .build();
    }
}
