package com.andrem91.ProductComparisonAPI.Service.analyzer;

import org.springframework.stereotype.Component;

/** Identifica produto com maior capacidade de bateria. */
@Component
public class BatteryAnalyzer extends SpecificationCategoryAnalyzer {
    
    public BatteryAnalyzer() {
        super("Melhor Bateria", "Battery", "Maior capacidade de bateria", true);
    }
}
