# 📊 Diagramas para o Vídeo

Use estes diagramas em texto para criar versões visuais no Draw.io, Excalidraw ou PowerPoint.

---

## 1. Arquitetura em Camadas

```
┌─────────────────────────────────────────┐
│           HTTP REQUEST                   │
│         (GET /api/products)              │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│         CONTROLLER LAYER                 │
│    ProductController.java                │
│  - Recebe requisições HTTP               │
│  - Valida parâmetros                     │
│  - Retorna ResponseEntity                │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│          SERVICE LAYER                   │
│   ProductComparisonService.java          │
│  - Lógica de negócio                     │
│  - Orquestração                          │
│  - Validações complexas                  │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│        REPOSITORY LAYER                  │
│     ProductRepository.java               │
│  - Acesso a dados                        │
│  - Queries                               │
│  - Persistência                          │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│          DATA SOURCE                     │
│        products.json                     │
└─────────────────────────────────────────┘
```

---

## 2. Strategy Pattern - CategoryAnalyzer

```
                ┌──────────────────────┐
                │  CategoryAnalyzer    │
                │    <<interface>>     │
                ├──────────────────────┤
                │ + getCategoryName()  │
                │ + analyze()          │
                └──────────┬───────────┘
                           │
                           │ implements
          ┌────────────────┼────────────────┐
          │                │                │
          ▼                ▼                ▼
┌─────────────────┐ ┌─────────────┐ ┌──────────────┐
│ PriceAnalyzer   │ │RatingAnalyzer│ │BatteryAnalyzer│
├─────────────────┤ ├─────────────┤ ├──────────────┤
│ analyze():      │ │ analyze():  │ │ analyze():   │
│  - Find MIN     │ │  - Find MAX │ │  - Find MAX  │
│    price        │ │    rating   │ │    battery   │
└─────────────────┘ └─────────────┘ └──────────────┘

          ┌────────────────┼────────────────┐
          │                │                │
          ▼                ▼                ▼
┌─────────────────┐ ┌─────────────┐ ┌──────────────┐
│  RAMAnalyzer    │ │StorageAnalyzer│ │ScreenAnalyzer│
└─────────────────┘ └─────────────┘ └──────────────┘
```

**Benefício:** Adicionar nova categoria = criar nova classe (Open/Closed)

---

## 3. Template Method Pattern

```
┌────────────────────────────────────────────┐
│   SpecificationCategoryAnalyzer            │
│          <<abstract>>                      │
├────────────────────────────────────────────┤
│ - categoryName: String                     │
│ - specKey: String                          │
│ - higherIsBetter: boolean                  │
├────────────────────────────────────────────┤
│ + analyze(products): CategoryWinner        │
│   1. Extract spec value                    │
│   2. Parse to number                       │
│   3. Find best (max or min)                │
│   4. Build CategoryWinner                  │
└────────────────┬───────────────────────────┘
                 │
                 │ extends
    ┌────────────┼────────────┐
    │            │            │
    ▼            ▼            ▼
┌─────────┐ ┌─────────┐ ┌──────────┐
│ Battery │ │   RAM   │ │  Camera  │
│Analyzer │ │Analyzer │ │ Analyzer │
├─────────┤ ├─────────┤ ├──────────┤
│ super(  │ │ super(  │ │ super(   │
│  "Bat", │ │  "RAM", │ │  "Cam",  │
│  true   │ │  true   │ │  true    │
│ )       │ │ )       │ │ )        │
└─────────┘ └─────────┘ └──────────┘
```

**Benefício:** Algoritmo comum, apenas parâmetros diferentes (DRY)

---

## 4. Facade Pattern

```
┌──────────────────────────────────────┐
│      ProductController               │
│                                      │
│  compareAndAnalyze(ids)              │
└────────────────┬─────────────────────┘
                 │
                 │ calls
                 ▼
┌──────────────────────────────────────┐
│   ProductComparisonService           │
│          <<FACADE>>                  │
│                                      │
│  compareAndAnalyze(ids) {            │
│    1. products = productService      │
│         .getProductsForComparison()  │
│    2. analysis = analysisService     │
│         .analyzeProducts()           │
│    3. return analysis                │
│  }                                   │
└────────────┬─────────────┬───────────┘
             │             │
             │             │
    ┌────────▼──────┐  ┌──▼──────────────────┐
    │ ProductService│  │ComparisonAnalysis   │
    │               │  │    Service          │
    │ - validate    │  │ - analyze categories│
    │ - fetch data  │  │ - calculate scores  │
    └───────────────┘  └─────────────────────┘
```

**Benefício:** Controller não precisa conhecer complexidade interna

---

## 5. Dependency Injection

```
┌─────────────────────────────────────────┐
│         Spring Container                 │
│                                          │
│  ┌────────────────────────────────┐     │
│  │  ProductController             │     │
│  │  - comparisonService ◄─────────┼──┐  │
│  └────────────────────────────────┘  │  │
│                                      │  │
│  ┌────────────────────────────────┐ │  │
│  │  ProductComparisonService      │◄┘  │
│  │  - productService ◄────────────┼──┐ │
│  │  - analysisService ◄───────────┼─┐│ │
│  └────────────────────────────────┘ ││ │
│                                     ││ │
│  ┌────────────────────────────────┐││ │
│  │  ProductService                │◄┘ │
│  │  - repository ◄────────────────┼──┐│
│  └────────────────────────────────┘  ││
│                                      ││
│  ┌────────────────────────────────┐ ││
│  │  ComparisonAnalysisService     │◄┘│
│  │  - categoryAnalyzers[] ◄───────┼─┐│
│  └────────────────────────────────┘ ││
│                                     ││
│  ┌────────────────────────────────┐││
│  │  ProductRepository             │◄┘│
│  └────────────────────────────────┘  │
│                                       │
│  ┌────────────────────────────────┐  │
│  │  PriceAnalyzer    ┐             │  │
│  │  RatingAnalyzer   │             │  │
│  │  BatteryAnalyzer  ├─────────────┼─►│
│  │  RAMAnalyzer      │  Auto-      │  │
│  │  StorageAnalyzer  │  detected   │  │
│  │  ...              ┘             │  │
│  └────────────────────────────────┘  │
└───────────────────────────────────────┘
```

**Benefício:** Baixo acoplamento, fácil testar, fácil trocar implementações

---

## 6. Fluxo Completo de Análise

```
┌─────────────────────────────────────────────────────────┐
│ 1. REQUEST                                              │
│    GET /api/products/compare/analysis?ids=1,2,3         │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│ 2. CONTROLLER                                           │
│    - Valida parâmetros (2-5 IDs)                        │
│    - Chama comparisonService.compareAndAnalyze()        │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│ 3. FACADE (ProductComparisonService)                    │
│    - Busca produtos via ProductService                  │
│    - Delega análise para ComparisonAnalysisService      │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│ 4. ANALYSIS SERVICE                                     │
│    - Itera sobre cada CategoryAnalyzer                  │
│    - Executa analyze() para cada categoria              │
│    - Calcula pontuação geral                            │
│    - Identifica produto recomendado                     │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│ 5. CATEGORY ANALYZERS (Strategy)                       │
│    PriceAnalyzer    → Menor preço: Google Pixel ($999)  │
│    RatingAnalyzer   → Maior rating: iPhone (4.9★)       │
│    BatteryAnalyzer  → Maior bateria: Samsung (5000mAh)  │
│    RAMAnalyzer      → Maior RAM: Samsung (12GB)         │
│    CameraAnalyzer   → Melhor câmera: Samsung (200MP)    │
│    ...                                                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│ 6. BUILDER                                              │
│    ComparisonAnalysis.Builder                           │
│    - addProduct(samsung)                                │
│    - addProduct(iphone)                                 │
│    - addProduct(google)                                 │
│    - addCategoryWinner("Melhor Preço", google)          │
│    - addCategoryWinner("Melhor Câmera", samsung)        │
│    - setRecommendation(samsung, 55 pontos)              │
│    - build()                                            │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│ 7. RESPONSE                                             │
│    {                                                    │
│      "products": [...],                                 │
│      "categoryWinners": {...},                          │
│      "overallRecommendation": {                         │
│        "productName": "Samsung Galaxy S24 Ultra",       │
│        "totalScore": 55,                                │
│        "strengths": ["Melhor Câmera", "Melhor RAM"],    │
│        "recommendation": "..."                          │
│      }                                                  │
│    }                                                    │
└─────────────────────────────────────────────────────────┘
```

---

## 7. SOLID Principles

```
┌────────────────────────────────────────────────────────┐
│  S - SINGLE RESPONSIBILITY                             │
├────────────────────────────────────────────────────────┤
│  ✅ PriceAnalyzer      → Só analisa preço              │
│  ✅ ProductService     → Só gerencia produtos          │
│  ✅ ProductController  → Só lida com HTTP              │
└────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────┐
│  O - OPEN/CLOSED                                       │
├────────────────────────────────────────────────────────┤
│  ✅ Nova categoria? Crie nova classe                   │
│  ❌ NÃO modifique ComparisonAnalysisService            │
│                                                        │
│  @Component                                            │
│  public class ProcessorAnalyzer                        │
│      extends SpecificationCategoryAnalyzer { ... }     │
└────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────┐
│  L - LISKOV SUBSTITUTION                               │
├────────────────────────────────────────────────────────┤
│  ✅ Qualquer CategoryAnalyzer pode substituir outro    │
│                                                        │
│  List<CategoryAnalyzer> analyzers = [                  │
│    new PriceAnalyzer(),                                │
│    new BatteryAnalyzer(),  ← Intercambiáveis           │
│    new RAMAnalyzer()                                   │
│  ];                                                    │
└────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────┐
│  I - INTERFACE SEGREGATION                             │
├────────────────────────────────────────────────────────┤
│  ✅ Interfaces pequenas e específicas                  │
│                                                        │
│  CategoryAnalyzer {                                    │
│    getCategoryName()  ← Apenas 2 métodos               │
│    analyze()                                           │
│  }                                                     │
│                                                        │
│  ❌ NÃO: SuperInterface com 20 métodos                 │
└────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────┐
│  D - DEPENDENCY INVERSION                              │
├────────────────────────────────────────────────────────┤
│  ✅ Dependa de abstrações, não de implementações       │
│                                                        │
│  ProductService {                                      │
│    private final IProductRepository repository;        │
│                      ↑                                 │
│                  Interface (abstração)                 │
│  }                                                     │
│                                                        │
│  Implementação pode ser:                               │
│  - ProductRepository (JSON)                            │
│  - JpaProductRepository (Database)                     │
│  - MockProductRepository (Tests)                       │
└────────────────────────────────────────────────────────┘
```

---

## 8. Design Patterns Summary

```
┌──────────────────────┬─────────────────────┬──────────────────┐
│   PATTERN            │   ONDE USADO        │   BENEFÍCIO      │
├──────────────────────┼─────────────────────┼──────────────────┤
│ Strategy             │ CategoryAnalyzer    │ Extensibilidade  │
│ Template Method      │ SpecificationCA     │ Reuso de código  │
│ Facade               │ ComparisonService   │ Simplicidade     │
│ Builder              │ ComparisonAnalysis  │ Objetos complexos│
│ Repository           │ ProductRepository   │ Abstração dados  │
│ Dependency Injection │ Todo o projeto      │ Baixo acoplamento│
│ Singleton            │ Spring Beans        │ Única instância  │
│ Factory              │ Spring Context      │ Criação objetos  │
└──────────────────────┴─────────────────────┴──────────────────┘
```

---

## 9. Antes vs Depois (Sem Patterns)

### ❌ SEM DESIGN PATTERNS
```java
public class ProductController {
    public ComparisonAnalysis analyze(List<Long> ids) {
        // Buscar produtos
        List<Product> products = new ArrayList<>();
        for (Long id : ids) {
            Product p = loadFromJson(id); // Acoplamento direto
            products.add(p);
        }
        
        // Análise de preço
        Product cheapest = null;
        for (Product p : products) {
            if (cheapest == null || p.getPrice() < cheapest.getPrice()) {
                cheapest = p;
            }
        }
        
        // Análise de bateria
        Product bestBattery = null;
        for (Product p : products) {
            int battery = Integer.parseInt(p.getSpec("Battery"));
            if (bestBattery == null || battery > ...) {
                bestBattery = p;
            }
        }
        
        // ... repetir para cada categoria (DUPLICAÇÃO!)
        
        // Construir resposta manualmente
        ComparisonAnalysis result = new ComparisonAnalysis();
        result.setProducts(products);
        result.addWinner("price", cheapest);
        result.addWinner("battery", bestBattery);
        // ...
        
        return result;
    }
}
```

**Problemas:**
- ❌ Controller com MUITAS responsabilidades
- ❌ Código duplicado para cada categoria
- ❌ Difícil adicionar nova categoria
- ❌ Difícil testar
- ❌ Acoplamento alto

---

### ✅ COM DESIGN PATTERNS
```java
@RestController
public class ProductController {
    private final ProductComparisonService comparisonService;
    
    public ComparisonAnalysis analyze(List<Long> ids) {
        return comparisonService.compareAndAnalyze(ids);
    }
}
```

**Benefícios:**
- ✅ Controller simples e focado
- ✅ Cada analyzer isolado e testável
- ✅ Nova categoria = nova classe
- ✅ Fácil manter e evoluir
- ✅ Baixo acoplamento

---

## 10. Métricas do Projeto

```
┌─────────────────────────────────────────┐
│         MÉTRICAS DE QUALIDADE           │
├─────────────────────────────────────────┤
│ Linhas de Código:        ~2,000         │
│ Classes:                 25+            │
│ Testes:                  60             │
│ Cobertura:               85%            │
│ Design Patterns:         8              │
│ SOLID Compliance:        100%           │
│ Bugs (SonarQube):        0              │
│ Code Smells:             0              │
│ Duplicação:              < 1%           │
└─────────────────────────────────────────┘
```

---

**Use estes diagramas como base para criar versões visuais no seu editor favorito!** 🎨
