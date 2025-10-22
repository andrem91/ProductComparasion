package com.andrem91.ProductComparisonAPI.Repository;

import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para ProductRepository.
 * Testa a recuperação de dados do arquivo JSON.
 */
@DisplayName("ProductRepository - Testes Unitários")
class ProductRepositoryTest {
    
    private ProductRepository productRepository;
    
    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        productRepository = new ProductRepository(objectMapper);
        productRepository.init(); // Inicializa manualmente para o teste
    }
    
    @Test
    @DisplayName("Deve carregar produtos do arquivo JSON")
    void shouldLoadProductsFromJson() {
        // Act
        List<ProductEntity> products = productRepository.findAll();
        
        // Assert
        assertThat(products).isNotNull();
        assertThat(products).isNotEmpty();
        assertThat(products.size()).isGreaterThanOrEqualTo(15); // Temos 15 produtos
    }
    
    @Test
    @DisplayName("Deve retornar todos os produtos")
    void shouldReturnAllProducts() {
        // Act
        List<ProductEntity> products = productRepository.findAll();
        
        // Assert
        assertThat(products).allMatch(p -> p.getId() != null);
        assertThat(products).allMatch(p -> p.getName() != null && !p.getName().isEmpty());
        assertThat(products).allMatch(p -> p.getPrice() > 0);
        assertThat(products).allMatch(p -> p.getRating() >= 0 && p.getRating() <= 5);
        assertThat(products).allMatch(p -> p.getSpecifications() != null && !p.getSpecifications().isEmpty());
    }
    
    @Test
    @DisplayName("Deve encontrar produto por ID quando existir")
    void shouldFindProductByIdWhenExists() {
        // Act
        Optional<ProductEntity> product = productRepository.findById(1L);
        
        // Assert
        assertThat(product).isPresent();
        assertThat(product.get().getId()).isEqualTo(1L);
        assertThat(product.get().getName()).isNotEmpty();
        assertThat(product.get().getSpecifications()).containsKey("Brand");
    }
    
    @Test
    @DisplayName("Deve retornar Optional vazio quando produto não existir")
    void shouldReturnEmptyOptionalWhenProductNotExists() {
        // Act
        Optional<ProductEntity> product = productRepository.findById(999L);
        
        // Assert
        assertThat(product).isEmpty();
    }
    
    @Test
    @DisplayName("Deve encontrar múltiplos produtos por IDs")
    void shouldFindMultipleProductsByIds() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        
        // Act
        List<ProductEntity> products = productRepository.findByIds(ids);
        
        // Assert
        assertThat(products).hasSize(3);
        assertThat(products).extracting(ProductEntity::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }
    
    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum ID corresponder")
    void shouldReturnEmptyListWhenNoIdsMatch() {
        // Arrange
        List<Long> ids = Arrays.asList(999L, 998L);
        
        // Act
        List<ProductEntity> products = productRepository.findByIds(ids);
        
        // Assert
        assertThat(products).isEmpty();
    }
    
    @Test
    @DisplayName("Deve retornar apenas produtos encontrados quando alguns IDs não existirem")
    void shouldReturnOnlyFoundProductsWhenSomeIdsNotExist() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 999L);
        
        // Act
        List<ProductEntity> products = productRepository.findByIds(ids);
        
        // Assert
        assertThat(products).hasSize(2);
        assertThat(products).extracting(ProductEntity::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }
    
    @Test
    @DisplayName("Deve contar total de produtos corretamente")
    void shouldCountTotalProductsCorrectly() {
        // Act
        long count = productRepository.count();
        
        // Assert
        assertThat(count).isGreaterThanOrEqualTo(15);
        assertThat(count).isEqualTo(productRepository.findAll().size());
    }
    
    @Test
    @DisplayName("Produtos devem ter todas as especificações necessárias")
    void productsShouldHaveAllNecessarySpecifications() {
        // Act
        List<ProductEntity> products = productRepository.findAll();
        
        // Assert
        assertThat(products).allMatch(p -> 
            p.getSpecifications().containsKey("Brand") &&
            p.getSpecifications().containsKey("Screen Size") &&
            p.getSpecifications().containsKey("Storage") &&
            p.getSpecifications().containsKey("RAM") &&
            p.getSpecifications().containsKey("Camera") &&
            p.getSpecifications().containsKey("Battery") &&
            p.getSpecifications().containsKey("Processor") &&
            p.getSpecifications().containsKey("OS")
        );
    }
    
    @Test
    @DisplayName("Deve carregar Samsung Galaxy S24 Ultra corretamente")
    void shouldLoadSamsungS24UltraCorrectly() {
        // Act
        Optional<ProductEntity> product = productRepository.findById(1L);
        
        // Assert
        assertThat(product).isPresent();
        ProductEntity samsung = product.get();
        assertThat(samsung.getName()).isEqualTo("Samsung Galaxy S24 Ultra");
        assertThat(samsung.getSpecifications().get("Brand")).isEqualTo("Samsung");
        assertThat(samsung.getRating()).isGreaterThan(4.0);
    }
    
    @Test
    @DisplayName("Deve carregar iPhone 15 Pro Max corretamente")
    void shouldLoadIPhone15ProMaxCorrectly() {
        // Act
        Optional<ProductEntity> product = productRepository.findById(2L);
        
        // Assert
        assertThat(product).isPresent();
        ProductEntity iphone = product.get();
        assertThat(iphone.getName()).isEqualTo("iPhone 15 Pro Max");
        assertThat(iphone.getSpecifications().get("Brand")).isEqualTo("Apple");
        assertThat(iphone.getRating()).isGreaterThan(4.0);
    }
    
    @Test
    @DisplayName("Deve ter produtos de diferentes marcas")
    void shouldHaveProductsFromDifferentBrands() {
        // Act
        List<ProductEntity> products = productRepository.findAll();
        
        // Assert
        List<String> brands = products.stream()
                .map(p -> p.getSpecifications().get("Brand"))
                .distinct()
                .toList();
        
        assertThat(brands).hasSizeGreaterThan(5);
        assertThat(brands).contains("Samsung", "Apple", "Google");
    }
    
    @Test
    @DisplayName("Deve ter produtos em diferentes faixas de preço")
    void shouldHaveProductsInDifferentPriceRanges() {
        // Act
        List<ProductEntity> products = productRepository.findAll();
        
        // Assert
        assertThat(products).anyMatch(p -> p.getPrice() < 500);  // Budget
        assertThat(products).anyMatch(p -> p.getPrice() >= 500 && p.getPrice() < 1000);  // Mid-range
        assertThat(products).anyMatch(p -> p.getPrice() >= 1000);  // Premium
    }
    
    @Test
    @DisplayName("Todos os produtos devem ter ratings válidos")
    void allProductsShouldHaveValidRatings() {
        // Act
        List<ProductEntity> products = productRepository.findAll();
        
        // Assert
        assertThat(products).allMatch(p -> p.getRating() >= 0.0 && p.getRating() <= 5.0);
        assertThat(products).allMatch(p -> p.getRating() >= 4.0); // Todos são bons produtos
    }
    
    @Test
    @DisplayName("Todos os produtos devem ter preços positivos")
    void allProductsShouldHavePositivePrices() {
        // Act
        List<ProductEntity> products = productRepository.findAll();
        
        // Assert
        assertThat(products).allMatch(p -> p.getPrice() > 0);
    }
}
