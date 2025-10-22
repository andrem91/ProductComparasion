package com.andrem91.ProductComparisonAPI.Service.scoring;

import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Identifica pontos fracos comparando com outros produtos.
 */
@Component
public class ProductWeaknessAnalyzer {
    
    /** Identifica pontos fracos do produto. */
    public List<String> identifyWeaknesses(ProductEntity product, List<ProductEntity> allProducts) {
        List<String> weaknesses = new ArrayList<>();
        
        if (allProducts.size() <= 1) {
            return weaknesses;
        }
        
        // Verifica se é o mais caro
        if (isMostExpensive(product, allProducts)) {
            weaknesses.add("Preço mais alto entre os comparados");
        }
        
        // Verifica se tem menor bateria
        if (hasSmallestBattery(product, allProducts)) {
            weaknesses.add("Menor capacidade de bateria");
        }
        
        // Verifica se tem menor RAM
        if (hasSmallestRAM(product, allProducts)) {
            weaknesses.add("Menor quantidade de RAM");
        }
        
        return weaknesses;
    }
    
    private boolean isMostExpensive(ProductEntity product, List<ProductEntity> allProducts) {
        return allProducts.stream()
                .noneMatch(p -> p.getPrice() > product.getPrice());
    }
    
    private boolean hasSmallestBattery(ProductEntity product, List<ProductEntity> allProducts) {
        int productBattery = extractNumericValue(product.getSpecifications().get("Battery"));
        return allProducts.stream()
                .allMatch(p -> extractNumericValue(p.getSpecifications().get("Battery")) >= productBattery);
    }
    
    private boolean hasSmallestRAM(ProductEntity product, List<ProductEntity> allProducts) {
        int productRAM = extractNumericValue(product.getSpecifications().get("RAM"));
        return allProducts.stream()
                .allMatch(p -> extractNumericValue(p.getSpecifications().get("RAM")) >= productRAM);
    }
    
    /** Extrai valor numérico de string (ex: "12GB" -> 12). */
    private int extractNumericValue(String value) {
        if (value == null) return 0;
        
        String numericPart = value.replaceAll("[^0-9.]", "");
        
        try {
            return (int) Double.parseDouble(numericPart);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
