package com.andrem91.ProductComparisonAPI.Service.analyzer;

import org.springframework.stereotype.Component;

/** Identifica produto com maior capacidade de armazenamento. */
@Component
public class StorageAnalyzer extends SpecificationCategoryAnalyzer {
    
    public StorageAnalyzer() {
        super("Melhor Armazenamento", "Storage", "Maior capacidade de armazenamento", true);
    }
}
