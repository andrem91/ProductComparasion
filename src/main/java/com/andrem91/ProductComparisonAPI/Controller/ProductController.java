package com.andrem91.ProductComparisonAPI.Controller;

import com.andrem91.ProductComparisonAPI.DTO.ComparisonAnalysis;
import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import com.andrem91.ProductComparisonAPI.Exception.ProductNotFoundException;
import com.andrem91.ProductComparisonAPI.Service.ProductComparisonService;
import com.andrem91.ProductComparisonAPI.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para gerenciamento e comparação de produtos.
 * Usa injeção de dependência por construtor via @RequiredArgsConstructor.
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "Product Comparison", description = "API endpoints for product comparison and retrieval")
@RequiredArgsConstructor
@Validated
public class ProductController {
    
    private final ProductService productService;
    private final ProductComparisonService comparisonService;
    
    /**
     * Recupera todos os produtos disponíveis.
     * @return Lista completa de produtos com especificações, preços e avaliações
     */
    @Operation(
        summary = "Obter todos os produtos",
        description = "Retorna lista completa de produtos com especificações técnicas, preços e avaliações."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de produtos recuperada com sucesso",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = ProductEntity.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Recupera um produto específico por ID.
     * @param id ID do produto
     * @return Detalhes completos do produto
     * @throws ProductNotFoundException se o produto não existir
     */
    @Operation(
        summary = "Obter produto por ID",
        description = "Retorna informações detalhadas de um produto específico incluindo especificações técnicas, preço e avaliação."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Produto encontrado com sucesso",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = ProductEntity.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Produto não encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProductById(
            @Parameter(description = "ID do produto a recuperar", required = true, example = "1")
            @PathVariable 
            @NotNull(message = "ID do produto não pode ser nulo")
            @Positive(message = "ID do produto deve ser um número positivo")
            Long id) {
        ProductEntity product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ResponseEntity.ok(product);
    }
    
    /**
     * Recupera múltiplos produtos para comparação lado a lado.
     * @param ids Lista de IDs (2-5 produtos)
     * @return Lista de produtos com dados completos para comparação
     * @throws IllegalArgumentException se quantidade de IDs for inválida
     */
    @Operation(
        summary = "Comparar produtos",
        description = "Compara de 2 a 5 produtos simultaneamente. Retorna dados completos para comparação de especificações, preços e avaliações."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Produtos recuperados com sucesso para comparação",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = ProductEntity.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Quantidade inválida de IDs (requer 2-5)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Um ou mais produtos não encontrados",
            content = @Content
        )
    })
    @GetMapping("/compare")
    public ResponseEntity<List<ProductEntity>> compareProducts(
            @Parameter(
                description = "Lista de IDs de produtos para comparar (2-5 IDs). Exemplo: 1,2,3 para comparar Samsung S24 Ultra, iPhone 15 Pro Max e Google Pixel 8 Pro", 
                required = true, 
                example = "1,2,3"
            )
            @RequestParam List<Long> ids) {
        
        // Valida e recupera produtos via camada de serviço
        List<ProductEntity> products = productService.getProductsForComparison(ids);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Analisa produtos e identifica o melhor em cada categoria.
     * Retorna vencedores por categoria, pontuação geral e recomendação final.
     * @param ids Lista de IDs (2-5 produtos)
     * @return Análise completa com vencedores, pontos fortes/fracos e recomendação
     * @throws IllegalArgumentException se quantidade de IDs for inválida
     */
    @Operation(
        summary = "Análise inteligente de comparação",
        description = "Identifica automaticamente o melhor produto em cada categoria (preço, bateria, câmera, RAM, etc). Retorna pontuação geral, pontos fortes/fracos e recomendação final."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Análise concluída com sucesso",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = ComparisonAnalysis.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Quantidade inválida de IDs (requer 2-5)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Um ou mais produtos não encontrados",
            content = @Content
        )
    })
    @GetMapping("/compare/analysis")
    public ResponseEntity<ComparisonAnalysis> compareProductsWithAnalysis(
            @Parameter(
                description = "Lista de IDs para análise (2-5 IDs). Exemplo: 1,2,3", 
                required = true, 
                example = "1,2,3"
            )
            @RequestParam List<Long> ids) {
        
        // Encapsula busca e análise em uma única chamada
        ComparisonAnalysis analysis = comparisonService.compareAndAnalyze(ids);
        
        return ResponseEntity.ok(analysis);
    }
}
