package com.andrem91.ProductComparisonAPI.Service;

import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import com.andrem91.ProductComparisonAPI.Exception.InvalidComparisonRequestException;
import com.andrem91.ProductComparisonAPI.Repository.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ProductService.
 * Testa a lógica de negócio relacionada a produtos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService - Testes Unitários")
class ProductServiceTest {
    
    @Mock
    private IProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    private ProductEntity product1;
    private ProductEntity product2;
    private ProductEntity product3;
    
    @BeforeEach
    void setUp() {
        // Configura produtos de teste
        product1 = new ProductEntity();
        product1.setId(1L);
        product1.setName("Samsung Galaxy S24 Ultra");
        product1.setPrice(1199.99);
        product1.setRating(4.8);
        product1.setSpecifications(Map.of("Brand", "Samsung", "RAM", "12GB"));
        
        product2 = new ProductEntity();
        product2.setId(2L);
        product2.setName("iPhone 15 Pro Max");
        product2.setPrice(1199.00);
        product2.setRating(4.9);
        product2.setSpecifications(Map.of("Brand", "Apple", "RAM", "8GB"));
        
        product3 = new ProductEntity();
        product3.setId(3L);
        product3.setName("Google Pixel 8 Pro");
        product3.setPrice(999.00);
        product3.setRating(4.7);
        product3.setSpecifications(Map.of("Brand", "Google", "RAM", "12GB"));
    }
    
    @Test
    @DisplayName("Deve retornar todos os produtos")
    void shouldReturnAllProducts() {
        // Arrange
        List<ProductEntity> expectedProducts = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(expectedProducts);
        
        // Act
        List<ProductEntity> actualProducts = productService.getAllProducts();
        
        // Assert
        assertThat(actualProducts).isNotNull();
        assertThat(actualProducts).hasSize(3);
        assertThat(actualProducts).containsExactlyElementsOf(expectedProducts);
        verify(productRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("Deve retornar produto por ID quando existir")
    void shouldReturnProductByIdWhenExists() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        
        // Act
        Optional<ProductEntity> result = productService.getProductById(1L);
        
        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(product1);
        assertThat(result.get().getName()).isEqualTo("Samsung Galaxy S24 Ultra");
        verify(productRepository, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("Deve retornar Optional vazio quando produto não existir")
    void shouldReturnEmptyOptionalWhenProductNotExists() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act
        Optional<ProductEntity> result = productService.getProductById(999L);
        
        // Assert
        assertThat(result).isEmpty();
        verify(productRepository, times(1)).findById(999L);
    }
    
    @Test
    @DisplayName("Deve retornar produtos para comparação quando IDs válidos")
    void shouldReturnProductsForComparisonWhenValidIds() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<ProductEntity> expectedProducts = Arrays.asList(product1, product2, product3);
        when(productRepository.findByIds(ids)).thenReturn(expectedProducts);
        
        // Act
        List<ProductEntity> result = productService.getProductsForComparison(ids);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(expectedProducts);
        verify(productRepository, times(1)).findByIds(ids);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando menos de 2 IDs fornecidos")
    void shouldThrowExceptionWhenLessThan2Ids() {
        // Arrange
        List<Long> ids = Collections.singletonList(1L);
        
        // Act & Assert
        assertThatThrownBy(() -> productService.getProductsForComparison(ids))
                .isInstanceOf(InvalidComparisonRequestException.class)
                .hasMessageContaining("At least 2 product IDs are required");
        
        verify(productRepository, never()).findByIds(any());
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando mais de 5 IDs fornecidos")
    void shouldThrowExceptionWhenMoreThan5Ids() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L);
        
        // Act & Assert
        assertThatThrownBy(() -> productService.getProductsForComparison(ids))
                .isInstanceOf(InvalidComparisonRequestException.class)
                .hasMessageContaining("Cannot compare more than 5 products");
        
        verify(productRepository, never()).findByIds(any());
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando IDs nulos")
    void shouldThrowExceptionWhenIdsNull() {
        // Act & Assert
        assertThatThrownBy(() -> productService.getProductsForComparison(null))
                .isInstanceOf(InvalidComparisonRequestException.class)
                .hasMessageContaining("At least 2 product IDs are required");
        
        verify(productRepository, never()).findByIds(any());
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nem todos os produtos são encontrados")
    void shouldThrowExceptionWhenNotAllProductsFound() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 999L);
        List<ProductEntity> foundProducts = Arrays.asList(product1, product2); // Apenas 2 de 3
        when(productRepository.findByIds(ids)).thenReturn(foundProducts);
        
        // Act & Assert
        assertThatThrownBy(() -> productService.getProductsForComparison(ids))
                .isInstanceOf(InvalidComparisonRequestException.class)
                .hasMessageContaining("One or more product IDs not found");
        
        verify(productRepository, times(1)).findByIds(ids);
    }
    
    @Test
    @DisplayName("Deve aceitar exatamente 2 produtos para comparação")
    void shouldAcceptExactly2ProductsForComparison() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);
        List<ProductEntity> expectedProducts = Arrays.asList(product1, product2);
        when(productRepository.findByIds(ids)).thenReturn(expectedProducts);
        
        // Act
        List<ProductEntity> result = productService.getProductsForComparison(ids);
        
        // Assert
        assertThat(result).hasSize(2);
        verify(productRepository, times(1)).findByIds(ids);
    }
    
    @Test
    @DisplayName("Deve aceitar exatamente 5 produtos para comparação")
    void shouldAcceptExactly5ProductsForComparison() {
        // Arrange
        ProductEntity product4 = new ProductEntity();
        product4.setId(4L);
        ProductEntity product5 = new ProductEntity();
        product5.setId(5L);
        
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<ProductEntity> expectedProducts = Arrays.asList(product1, product2, product3, product4, product5);
        when(productRepository.findByIds(ids)).thenReturn(expectedProducts);
        
        // Act
        List<ProductEntity> result = productService.getProductsForComparison(ids);
        
        // Assert
        assertThat(result).hasSize(5);
        verify(productRepository, times(1)).findByIds(ids);
    }
}
