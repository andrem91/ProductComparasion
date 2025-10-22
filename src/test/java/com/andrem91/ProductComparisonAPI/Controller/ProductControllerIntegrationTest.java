package com.andrem91.ProductComparisonAPI.Controller;

import com.andrem91.ProductComparisonAPI.Entity.ProductEntity;
import com.andrem91.ProductComparisonAPI.Repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para ProductController.
 * Testa os endpoints da API com contexto Spring completo.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductController - Testes de Integração")
class ProductControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Test
    @DisplayName("GET /api/products - Deve retornar todos os produtos")
    void shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].price", notNullValue()))
                .andExpect(jsonPath("$[0].rating", notNullValue()))
                .andExpect(jsonPath("$[0].specifications", notNullValue()));
    }
    
    @Test
    @DisplayName("GET /api/products/{id} - Deve retornar produto por ID quando existir")
    void shouldReturnProductByIdWhenExists() throws Exception {
        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.price", greaterThan(0.0)))
                .andExpect(jsonPath("$.rating", greaterThan(0.0)))
                .andExpect(jsonPath("$.specifications", notNullValue()));
    }
    
    @Test
    @DisplayName("GET /api/products/{id} - Deve retornar 404 quando produto não existir")
    void shouldReturn404WhenProductNotExists() throws Exception {
        mockMvc.perform(get("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("999")));
    }
    
    @Test
    @DisplayName("GET /api/products/compare - Deve comparar produtos com IDs válidos")
    void shouldCompareProductsWithValidIds() throws Exception {
        mockMvc.perform(get("/api/products/compare")
                        .param("ids", "1", "2", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));
    }
    
    @Test
    @DisplayName("GET /api/products/compare - Deve retornar 400 com menos de 2 IDs")
    void shouldReturn400WithLessThan2Ids() throws Exception {
        mockMvc.perform(get("/api/products/compare")
                        .param("ids", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("At least 2 product IDs")));
    }
    
    @Test
    @DisplayName("GET /api/products/compare - Deve retornar 400 com mais de 5 IDs")
    void shouldReturn400WithMoreThan5Ids() throws Exception {
        mockMvc.perform(get("/api/products/compare")
                        .param("ids", "1", "2", "3", "4", "5", "6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Cannot compare more than 5")));
    }
    
    @Test
    @DisplayName("GET /api/products/compare - Deve retornar 400 quando produto não encontrado")
    void shouldReturn400WhenProductNotFound() throws Exception {
        mockMvc.perform(get("/api/products/compare")
                        .param("ids", "1", "2", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("not found")));
    }
    
    @Test
    @DisplayName("GET /api/products/compare - Deve aceitar exatamente 2 produtos")
    void shouldAcceptExactly2Products() throws Exception {
        mockMvc.perform(get("/api/products/compare")
                        .param("ids", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
    
    @Test
    @DisplayName("GET /api/products/compare - Deve aceitar exatamente 5 produtos")
    void shouldAcceptExactly5Products() throws Exception {
        mockMvc.perform(get("/api/products/compare")
                        .param("ids", "1", "2", "3", "4", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }
    
    @Test
    @DisplayName("GET /api/products/compare/analysis - Deve retornar análise completa")
    void shouldReturnCompleteAnalysis() throws Exception {
        mockMvc.perform(get("/api/products/compare/analysis")
                        .param("ids", "1", "2", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.products", hasSize(3)))
                .andExpect(jsonPath("$.categoryWinners", notNullValue()))
                .andExpect(jsonPath("$.overallRecommendation", notNullValue()))
                .andExpect(jsonPath("$.overallRecommendation.productId", notNullValue()))
                .andExpect(jsonPath("$.overallRecommendation.productName", notNullValue()))
                .andExpect(jsonPath("$.overallRecommendation.totalScore", greaterThan(0)))
                .andExpect(jsonPath("$.overallRecommendation.recommendation", notNullValue()))
                .andExpect(jsonPath("$.overallRecommendation.strengths", notNullValue()))
                .andExpect(jsonPath("$.overallRecommendation.weaknesses", notNullValue()));
    }
    
    @Test
    @DisplayName("GET /api/products/compare/analysis - Deve incluir vencedores por categoria")
    void shouldIncludeCategoryWinners() throws Exception {
        mockMvc.perform(get("/api/products/compare/analysis")
                        .param("ids", "1", "2", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryWinners['Melhor Preço']", notNullValue()))
                .andExpect(jsonPath("$.categoryWinners['Melhor Avaliação']", notNullValue()))
                .andExpect(jsonPath("$.categoryWinners['Melhor Bateria']", notNullValue()))
                .andExpect(jsonPath("$.categoryWinners['Melhor Câmera']", notNullValue()))
                .andExpect(jsonPath("$.categoryWinners['Melhor RAM']", notNullValue()))
                .andExpect(jsonPath("$.categoryWinners['Melhor Tela']", notNullValue()));
    }
    
    @Test
    @DisplayName("GET /api/products/compare/analysis - Vencedores devem ter estrutura correta")
    void categoryWinnersShouldHaveCorrectStructure() throws Exception {
        mockMvc.perform(get("/api/products/compare/analysis")
                        .param("ids", "1", "2", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryWinners['Melhor Preço'].winnerId", notNullValue()))
                .andExpect(jsonPath("$.categoryWinners['Melhor Preço'].winnerName", notNullValue()))
                .andExpect(jsonPath("$.categoryWinners['Melhor Preço'].value", notNullValue()))
                .andExpect(jsonPath("$.categoryWinners['Melhor Preço'].reason", notNullValue()));
    }
    
    @Test
    @DisplayName("GET /api/products/compare/analysis - Deve retornar 400 com IDs inválidos")
    void analysisShouldReturn400WithInvalidIds() throws Exception {
        mockMvc.perform(get("/api/products/compare/analysis")
                        .param("ids", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("GET /api/products/compare/analysis - Deve funcionar com 2 produtos")
    void analysisShouldWorkWith2Products() throws Exception {
        mockMvc.perform(get("/api/products/compare/analysis")
                        .param("ids", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.overallRecommendation", notNullValue()));
    }
    
    @Test
    @DisplayName("GET /api/products/compare/analysis - Deve funcionar com 5 produtos")
    void analysisShouldWorkWith5Products() throws Exception {
        mockMvc.perform(get("/api/products/compare/analysis")
                        .param("ids", "1", "2", "3", "4", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(5)))
                .andExpect(jsonPath("$.overallRecommendation", notNullValue()));
    }
    
    @Test
    @DisplayName("Deve ter CORS habilitado")
    void shouldHaveCorsEnabled() throws Exception {
        mockMvc.perform(get("/api/products")
                        .header("Origin", "http://localhost:3000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
    
    @Test
    @DisplayName("Deve retornar Content-Type application/json")
    void shouldReturnJsonContentType() throws Exception {
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
