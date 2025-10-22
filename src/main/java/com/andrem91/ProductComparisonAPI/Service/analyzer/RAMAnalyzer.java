package com.andrem91.ProductComparisonAPI.Service.analyzer;

import org.springframework.stereotype.Component;

/** Identifica produto com maior quantidade de RAM. */
@Component
public class RAMAnalyzer extends SpecificationCategoryAnalyzer {
    
    public RAMAnalyzer() {
        super("Melhor RAM", "RAM", "Maior quantidade de memória RAM", true);
    }
}
