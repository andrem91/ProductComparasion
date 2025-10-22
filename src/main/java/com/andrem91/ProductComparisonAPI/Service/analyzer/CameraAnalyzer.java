package com.andrem91.ProductComparisonAPI.Service.analyzer;

import org.springframework.stereotype.Component;

/** Identifica produto com maior resolução de câmera. */
@Component
public class CameraAnalyzer extends SpecificationCategoryAnalyzer {
    
    public CameraAnalyzer() {
        super("Melhor Câmera", "Camera", "Maior resolução de câmera", true);
    }
}
