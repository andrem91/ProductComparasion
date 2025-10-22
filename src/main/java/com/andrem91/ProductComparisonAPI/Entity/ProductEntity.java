package com.andrem91.ProductComparisonAPI.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Entidade de produto com detalhes para comparação.
 * Persistência baseada em JSON.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Product entity containing all product information for comparison")
public class ProductEntity {
    
    @Schema(description = "Unique identifier for the product", example = "1")
    private Long id;
    
    @Schema(description = "Product name", example = "Samsung Galaxy S24 Ultra")
    private String name;
    
    @Schema(description = "URL to the product image", example = "https://images.example.com/samsung-s24-ultra.jpg")
    private String imageUrl;
    
    @Schema(description = "Detailed description of the product", 
            example = "Premium flagship smartphone with advanced camera system and S Pen support")
    private String description;
    
    @Schema(description = "Product price in USD", example = "1199.99")
    private double price;
    
    @Schema(description = "Product rating from 0.0 to 5.0", example = "4.8", minimum = "0.0", maximum = "5.0")
    private double rating;
    
    @Schema(description = "Key-value pairs for product specifications", 
            example = "{\"Brand\": \"Samsung\", \"Screen Size\": \"6.8 inches\", \"Storage\": \"256GB\"}")
    private Map<String, String> specifications;
}
