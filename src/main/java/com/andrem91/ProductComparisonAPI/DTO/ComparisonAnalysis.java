package com.andrem91.ProductComparisonAPI.DTO;

import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO para análise de comparação de produtos.
 * Identifica o melhor produto em cada categoria.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComparisonAnalysis {
    
    /** Produtos sendo comparados */
    private List<ProductSummary> products;
    
    /** Vencedores por categoria */
    private Map<String, CategoryWinner> categoryWinners;
    
    /** Recomendação geral */
    private OverallRecommendation overallRecommendation;
    
    /** Resumo de produto na comparação. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSummary {
        private Long id;
        private String name;
        private String brand;
        private Double price;
        private Double rating;
        private Integer score; // Pontuação geral (0-100)
        
        /** Cria ProductSummary a partir de ProductEntity. */
        public static ProductSummary from(ProductEntity product) {
            return ProductSummary.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .brand(product.getSpecifications().get("Brand"))
                    .price(product.getPrice())
                    .rating(product.getRating())
                    .score(0)
                    .build();
        }
        
        /** Cria ProductSummary com score definido. */
        public static ProductSummary from(ProductEntity product, int score) {
            return ProductSummary.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .brand(product.getSpecifications().get("Brand"))
                    .price(product.getPrice())
                    .rating(product.getRating())
                    .score(score)
                    .build();
        }
    }
    
    /** Vencedor em uma categoria específica. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryWinner {
        private String category;
        private Long winnerId;
        private String winnerName;
        private String value;
        private String reason;
        
        /** Retorna CategoryWinner vazio (Null Object Pattern). */
        public static CategoryWinner empty(String category) {
            return CategoryWinner.builder()
                    .category(category)
                    .winnerId(null)
                    .winnerName("N/A")
                    .value("N/A")
                    .reason("Nenhum vencedor identificado")
                    .build();
        }
        
        /** Verifica se há vencedor. */
        public boolean isPresent() {
            return winnerId != null;
        }
        
        /** Verifica se está vazio. */
        public boolean isEmpty() {
            return winnerId == null;
        }
    }
    
    /** Recomendação geral baseada em todos os critérios. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverallRecommendation {
        private Long productId;
        private String productName;
        private Integer totalScore;
        private String recommendation;
        private List<String> strengths;
        private List<String> weaknesses;
    }
}
