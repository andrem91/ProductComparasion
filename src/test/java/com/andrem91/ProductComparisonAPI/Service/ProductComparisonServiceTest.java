package com.andrem91.ProductComparisonAPI.Service;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ProductComparisonService.
 * Verifica se o serviço está orquestrando corretamente a comparação.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductComparisonService Tests")
class ProductComparisonServiceTest {
    
    @Mock
    private ProductService productService;
    
    @Mock
    private ComparisonAnalysisService analysisService;
    
    @InjectMocks
    private ProductComparisonService comparisonService;
    
    private List<Long> productIds;
    private List<ProductEntity> products;
    private ComparisonAnalysis expectedAnalysis;
    
    @BeforeEach
    void setUp() {
        productIds = Arrays.asList(1L, 2L, 3L);
        
        products = Arrays.asList(
            createMockProduct(1L, "Product 1"),
            createMockProduct(2L, "Product 2"),
            createMockProduct(3L, "Product 3")
        );
        
        expectedAnalysis = new ComparisonAnalysis();
    }
    
    @Test
    @DisplayName("Deve comparar e analisar produtos em uma única chamada")
    void shouldCompareAndAnalyzeProducts() {
        // Arrange
        when(productService.getProductsForComparison(productIds)).thenReturn(products);
        when(analysisService.analyzeProducts(products)).thenReturn(expectedAnalysis);
        
        // Act
        ComparisonAnalysis result = comparisonService.compareAndAnalyze(productIds);
        
        // Assert
        assertThat(result).isEqualTo(expectedAnalysis);
        verify(productService, times(1)).getProductsForComparison(productIds);
        verify(analysisService, times(1)).analyzeProducts(products);
    }
    
    @Test
    @DisplayName("Deve analisar produtos já carregados")
    void shouldAnalyzeLoadedProducts() {
        // Arrange
        when(analysisService.analyzeProducts(products)).thenReturn(expectedAnalysis);
        
        // Act
        ComparisonAnalysis result = comparisonService.analyzeProducts(products);
        
        // Assert
        assertThat(result).isEqualTo(expectedAnalysis);
        verify(analysisService, times(1)).analyzeProducts(products);
        verify(productService, never()).getProductsForComparison(anyList());
    }
    
    @Test
    @DisplayName("Serviço deve encapsular corretamente a lógica de orquestração")
    void shouldEncapsulateOrchestrationLogic() {
        // Arrange
        when(productService.getProductsForComparison(productIds)).thenReturn(products);
        when(analysisService.analyzeProducts(products)).thenReturn(expectedAnalysis);
        
        // Act
        comparisonService.compareAndAnalyze(productIds);
        
        // Assert - Verifica ordem de chamadas
        var inOrder = inOrder(productService, analysisService);
        inOrder.verify(productService).getProductsForComparison(productIds);
        inOrder.verify(analysisService).analyzeProducts(products);
    }
    
    private ProductEntity createMockProduct(Long id, String name) {
        ProductEntity product = new ProductEntity();
        product.setId(id);
        product.setName(name);
        product.setPrice(999.99);
        product.setRating(4.5);
        return product;
    }
}
