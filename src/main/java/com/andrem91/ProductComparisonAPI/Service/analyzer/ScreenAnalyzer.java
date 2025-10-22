package com.andrem91.ProductComparisonAPI.Service.analyzer;

import org.springframework.stereotype.Component;

/** Identifica produto com maior tamanho de tela. */
@Component
public class ScreenAnalyzer extends SpecificationCategoryAnalyzer {
    
    public ScreenAnalyzer() {
        super("Melhor Tela", "Screen Size", "Maior tamanho de tela", true);
    }
}
