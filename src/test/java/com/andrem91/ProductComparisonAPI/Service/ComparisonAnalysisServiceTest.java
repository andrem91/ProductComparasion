package com.andrem91.ProductComparisonAPI.Service;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import com.andrem91.ProductComparisonAPI.Service.analyzer.*;
import com.andrem91.ProductComparisonAPI.Service.scoring.ProductScoreCalculator;
import com.andrem91.ProductComparisonAPI.Service.scoring.ProductStrengthAnalyzer;
import com.andrem91.ProductComparisonAPI.Service.scoring.ProductWeaknessAnalyzer;
import com.andrem91.ProductComparisonAPI.Service.scoring.RecommendationGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para ComparisonAnalysisService.
 * Testa a lógica de análise e comparação de produtos.
 */
@DisplayName("ComparisonAnalysisService - Testes Unitários")
class ComparisonAnalysisServiceTest {
    
    private ComparisonAnalysisService analysisService;
    
    private ProductEntity samsungS24;
    private ProductEntity iphone15;
    private ProductEntity pixel8;
    
    @BeforeEach
    void setUp() {
        // Cria todas as dependências manualmente
        List<CategoryAnalyzer> analyzers = List.of(
            new PriceAnalyzer(),
            new RatingAnalyzer(),
            new BatteryAnalyzer(),
            new RAMAnalyzer(),
            new StorageAnalyzer(),
            new ScreenAnalyzer(),
            new CameraAnalyzer()
        );
        
        ProductScoreCalculator scoreCalculator = new ProductScoreCalculator();
        ProductStrengthAnalyzer strengthAnalyzer = new ProductStrengthAnalyzer();
        ProductWeaknessAnalyzer weaknessAnalyzer = new ProductWeaknessAnalyzer();
        RecommendationGenerator recommendationGenerator = new RecommendationGenerator();
        
        analysisService = new ComparisonAnalysisService(
            analyzers,
            scoreCalculator,
            strengthAnalyzer,
            weaknessAnalyzer,
            recommendationGenerator
        );
        
        // Samsung Galaxy S24 Ultra - Melhor câmera e tela
        samsungS24 = new ProductEntity();
        samsungS24.setId(1L);
        samsungS24.setName("Samsung Galaxy S24 Ultra");
        samsungS24.setPrice(1199.99);
        samsungS24.setRating(4.8);
        Map<String, String> samsungSpecs = new HashMap<>();
        samsungSpecs.put("Brand", "Samsung");
        samsungSpecs.put("Screen Size", "6.8 inches");
        samsungSpecs.put("Storage", "256GB");
        samsungSpecs.put("RAM", "12GB");
        samsungSpecs.put("Camera", "200MP + 50MP + 12MP");
        samsungSpecs.put("Battery", "5000mAh");
        samsungS24.setSpecifications(samsungSpecs);
        
        // iPhone 15 Pro Max - Melhor avaliação
        iphone15 = new ProductEntity();
        iphone15.setId(2L);
        iphone15.setName("iPhone 15 Pro Max");
        iphone15.setPrice(1199.00);
        iphone15.setRating(4.9);
        Map<String, String> iphoneSpecs = new HashMap<>();
        iphoneSpecs.put("Brand", "Apple");
        iphoneSpecs.put("Screen Size", "6.7 inches");
        iphoneSpecs.put("Storage", "256GB");
        iphoneSpecs.put("RAM", "8GB");
        iphoneSpecs.put("Camera", "48MP + 12MP + 12MP");
        iphoneSpecs.put("Battery", "4422mAh");
        iphone15.setSpecifications(iphoneSpecs);
        
        // Google Pixel 8 Pro - Melhor preço e bateria
        pixel8 = new ProductEntity();
        pixel8.setId(3L);
        pixel8.setName("Google Pixel 8 Pro");
        pixel8.setPrice(999.00);
        pixel8.setRating(4.7);
        Map<String, String> pixelSpecs = new HashMap<>();
        pixelSpecs.put("Brand", "Google");
        pixelSpecs.put("Screen Size", "6.7 inches");
        pixelSpecs.put("Storage", "256GB");
        pixelSpecs.put("RAM", "12GB");
        pixelSpecs.put("Camera", "50MP + 48MP + 48MP");
        pixelSpecs.put("Battery", "5050mAh");
        pixel8.setSpecifications(pixelSpecs);
    }
    
    @Test
    @DisplayName("Deve analisar produtos e retornar análise completa")
    void shouldAnalyzeProductsAndReturnCompleteAnalysis() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        assertThat(analysis).isNotNull();
        assertThat(analysis.getProducts()).hasSize(3);
        assertThat(analysis.getCategoryWinners()).isNotEmpty();
        assertThat(analysis.getOverallRecommendation()).isNotNull();
    }
    
    @Test
    @DisplayName("Deve identificar melhor preço corretamente")
    void shouldIdentifyBestPriceCorrectly() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.CategoryWinner bestPrice = analysis.getCategoryWinners().get("Melhor Preço");
        assertThat(bestPrice).isNotNull();
        assertThat(bestPrice.getWinnerId()).isEqualTo(3L); // Google Pixel 8 Pro
        assertThat(bestPrice.getWinnerName()).isEqualTo("Google Pixel 8 Pro");
        assertThat(bestPrice.getValue()).containsAnyOf("999.00", "999,00");
    }
    
    @Test
    @DisplayName("Deve identificar melhor avaliação corretamente")
    void shouldIdentifyBestRatingCorrectly() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.CategoryWinner bestRating = analysis.getCategoryWinners().get("Melhor Avaliação");
        assertThat(bestRating).isNotNull();
        assertThat(bestRating.getWinnerId()).isEqualTo(2L); // iPhone 15 Pro Max
        assertThat(bestRating.getWinnerName()).isEqualTo("iPhone 15 Pro Max");
        assertThat(bestRating.getValue()).containsAnyOf("4.9", "4,9");
    }
    
    @Test
    @DisplayName("Deve identificar melhor bateria corretamente")
    void shouldIdentifyBestBatteryCorrectly() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.CategoryWinner bestBattery = analysis.getCategoryWinners().get("Melhor Bateria");
        assertThat(bestBattery).isNotNull();
        assertThat(bestBattery.getWinnerId()).isEqualTo(3L); // Google Pixel 8 Pro - 5050mAh
        assertThat(bestBattery.getValue()).contains("5050mAh");
    }
    
    @Test
    @DisplayName("Deve identificar melhor câmera corretamente")
    void shouldIdentifyBestCameraCorrectly() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.CategoryWinner bestCamera = analysis.getCategoryWinners().get("Melhor Câmera");
        assertThat(bestCamera).isNotNull();
        assertThat(bestCamera.getWinnerId()).isEqualTo(1L); // Samsung S24 Ultra - 200MP
        assertThat(bestCamera.getValue()).contains("200MP");
    }
    
    @Test
    @DisplayName("Deve identificar melhor RAM corretamente")
    void shouldIdentifyBestRAMCorrectly() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.CategoryWinner bestRAM = analysis.getCategoryWinners().get("Melhor RAM");
        assertThat(bestRAM).isNotNull();
        // Samsung e Pixel têm 12GB, um deles vence
        assertThat(bestRAM.getValue()).contains("12GB");
    }
    
    @Test
    @DisplayName("Deve identificar melhor tela corretamente")
    void shouldIdentifyBestScreenCorrectly() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.CategoryWinner bestScreen = analysis.getCategoryWinners().get("Melhor Tela");
        assertThat(bestScreen).isNotNull();
        assertThat(bestScreen.getWinnerId()).isEqualTo(1L); // Samsung S24 Ultra - 6.8 inches
        assertThat(bestScreen.getValue()).contains("6.8");
    }
    
    @Test
    @DisplayName("Deve gerar recomendação geral com produto vencedor")
    void shouldGenerateOverallRecommendationWithWinner() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.OverallRecommendation recommendation = analysis.getOverallRecommendation();
        assertThat(recommendation).isNotNull();
        assertThat(recommendation.getProductId()).isNotNull();
        assertThat(recommendation.getProductName()).isNotEmpty();
        assertThat(recommendation.getTotalScore()).isGreaterThan(0);
        assertThat(recommendation.getRecommendation()).isNotEmpty();
    }
    
    @Test
    @DisplayName("Deve incluir pontos fortes na recomendação")
    void shouldIncludeStrengthsInRecommendation() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.OverallRecommendation recommendation = analysis.getOverallRecommendation();
        assertThat(recommendation.getStrengths()).isNotNull();
        assertThat(recommendation.getStrengths()).isNotEmpty();
    }
    
    @Test
    @DisplayName("Deve incluir pontos fracos na recomendação")
    void shouldIncludeWeaknessesInRecommendation() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.OverallRecommendation recommendation = analysis.getOverallRecommendation();
        assertThat(recommendation.getWeaknesses()).isNotNull();
        // Pode estar vazio se o produto vencedor não tiver pontos fracos
    }
    
    @Test
    @DisplayName("Deve criar resumos de produtos com informações corretas")
    void shouldCreateProductSummariesWithCorrectInfo() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        List<ComparisonAnalysis.ProductSummary> summaries = analysis.getProducts();
        assertThat(summaries).hasSize(3);
        
        ComparisonAnalysis.ProductSummary summary1 = summaries.get(0);
        assertThat(summary1.getId()).isEqualTo(1L);
        assertThat(summary1.getName()).isEqualTo("Samsung Galaxy S24 Ultra");
        assertThat(summary1.getBrand()).isEqualTo("Samsung");
        assertThat(summary1.getPrice()).isEqualTo(1199.99);
        assertThat(summary1.getRating()).isEqualTo(4.8);
    }
    
    @Test
    @DisplayName("Deve calcular pontuação baseada em categorias vencidas")
    void shouldCalculateScoreBasedOnCategoriesWon() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        ComparisonAnalysis.OverallRecommendation recommendation = analysis.getOverallRecommendation();
        // Produto vencedor deve ter pontuação maior que 0
        assertThat(recommendation.getTotalScore()).isGreaterThan(0);
        // Cada categoria vencida vale 15 pontos + bônus
        assertThat(recommendation.getTotalScore()).isGreaterThanOrEqualTo(15);
    }
    
    @Test
    @DisplayName("Deve funcionar com apenas 2 produtos")
    void shouldWorkWithOnly2Products() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        assertThat(analysis).isNotNull();
        assertThat(analysis.getProducts()).hasSize(2);
        assertThat(analysis.getCategoryWinners()).isNotEmpty();
        assertThat(analysis.getOverallRecommendation()).isNotNull();
    }
    
    @Test
    @DisplayName("Deve gerar texto de recomendação personalizado")
    void shouldGeneratePersonalizedRecommendationText() {
        // Arrange
        List<ProductEntity> products = Arrays.asList(samsungS24, iphone15, pixel8);
        
        // Act
        ComparisonAnalysis analysis = analysisService.analyzeProducts(products);
        
        // Assert
        String recommendation = analysis.getOverallRecommendation().getRecommendation();
        assertThat(recommendation).isNotEmpty();
        assertThat(recommendation).contains("melhor escolha");
        assertThat(recommendation).containsAnyOf("Samsung", "iPhone", "Pixel");
    }
}
