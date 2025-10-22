package com.andrem91.ProductComparisonAPI.Repository;

import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositório para gerenciar dados de produtos do arquivo JSON.
 */
@Repository
@RequiredArgsConstructor
public class ProductRepository implements IProductRepository {
    
    private final ObjectMapper objectMapper;
    private List<ProductEntity> products;
    
    /** Inicializa carregando produtos do JSON. */
    @jakarta.annotation.PostConstruct
    public void init() {
        this.products = loadProductsFromJson();
    }
    
    /** Carrega produtos do arquivo products.json. */
    private List<ProductEntity> loadProductsFromJson() {
        try {
            // Carrega JSON
            ClassPathResource resource = new ClassPathResource("products.json");
            InputStream inputStream = resource.getInputStream();
            
            // Converte para lista
            return objectMapper.readValue(inputStream, new TypeReference<List<ProductEntity>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to load products from JSON file", e);
        }
    }
    
    /** Recupera todos os produtos. */
    public List<ProductEntity> findAll() {
        return products;
    }
    
    /** Encontra produto por ID. */
    public Optional<ProductEntity> findById(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }
    
    /** Recupera produtos por IDs. */
    public List<ProductEntity> findByIds(List<Long> ids) {
        return products.stream()
                .filter(product -> ids.contains(product.getId()))
                .collect(Collectors.toList());
    }
    
    /** Conta total de produtos. */
    public long count() {
        return products.size();
    }
}
