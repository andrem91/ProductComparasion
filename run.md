# 🚀 Como Executar o Projeto

Guia completo para executar a **API de Comparação de Produtos**.

---

## 📋 Pré-requisitos

- **Java 21 ou superior** ([Download](https://www.oracle.com/java/technologies/downloads/))
- **IDE** (IntelliJ IDEA, Eclipse, VS Code) ou Maven instalado

---

## ⚙️ Configuração e Execução

### Opção 1: Via IDE (Recomendado)

#### 1. Importar o Projeto
1. Abra sua IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Selecione **File → Open** ou **Import Project**
3. Navegue até a pasta do projeto
4. Selecione o arquivo `pom.xml`
5. Aguarde a IDE baixar as dependências Maven

#### 2. Executar a Aplicação
1. Localize o arquivo principal:
   ```
   src/main/java/com/andrem91/ProductComparisonAPI/ProductComparisonApiApplication.java
   ```
2. Clique com botão direito → **Run** ou clique no botão ▶️
3. Aguarde a mensagem:
   ```
   Started ProductComparisonApiApplication in X.XXX seconds
   ```

✅ **A API estará rodando em:** `http://localhost:8080`

---

### Opção 2: Via Maven (Linha de Comando)

#### Windows
```bash
cd C:\caminho\para\ProductComparison
.\mvnw.cmd spring-boot:run
```

#### Linux/Mac
```bash
cd /caminho/para/ProductComparison
./mvnw spring-boot:run
```

✅ **A API estará rodando em:** `http://localhost:8080`

---

## 🧪 Testar a API

### 1. Swagger UI (Recomendado) 📖

**Acesse a documentação interativa:**
```
http://localhost:8080/swagger-ui.html
```

**Como usar:**
1. Visualize todos os endpoints disponíveis
2. Clique em qualquer endpoint para expandir
3. Clique em **"Try it out"**
4. Preencha os parâmetros (se necessário)
5. Clique em **"Execute"**
6. Veja a resposta em tempo real

**Vantagens:**
- ✅ Interface visual intuitiva
- ✅ Testa diretamente no navegador
- ✅ Documentação completa dos endpoints
- ✅ Exemplos de requisição/resposta

---

### 2. Navegador Web 🌐

**Endpoints GET podem ser testados diretamente no navegador:**

```
http://localhost:8080/api/products
http://localhost:8080/api/products/1
http://localhost:8080/api/products/compare?ids=1,2,3
http://localhost:8080/api/products/compare/analysis?ids=1,2,3
```

---

### 3. cURL (Terminal) 💻

```bash
# Listar todos os produtos
curl http://localhost:8080/api/products

# Buscar produto por ID
curl http://localhost:8080/api/products/1

# Comparar produtos
curl "http://localhost:8080/api/products/compare?ids=1,2,3"

# Análise inteligente
curl "http://localhost:8080/api/products/compare/analysis?ids=1,2,3"
```

---

### 4. Postman 📮

1. **Download:** https://www.postman.com/downloads/
2. Crie uma nova requisição **GET**
3. Digite a URL: `http://localhost:8080/api/products`
4. Clique em **"Send"**

---

### 5. Insomnia 🌙

1. **Download:** https://insomnia.rest/download
2. Crie uma nova requisição **GET**
3. Digite a URL: `http://localhost:8080/api/products`
4. Clique em **"Send"**

---

## 📡 Endpoints Disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/products` | Lista todos os produtos (15 smartphones) |
| GET | `/api/products/{id}` | Busca produto específico por ID |
| GET | `/api/products/compare?ids=1,2,3` | Compara 2-5 produtos |
| GET | `/api/products/compare/analysis?ids=1,2,3` | **Análise inteligente** com recomendação |

---

## 🎯 Exemplos de Teste

### Teste 1: Listar Todos os Produtos
```
http://localhost:8080/api/products
```
**Resposta:** Array com 15 smartphones

### Teste 2: Buscar Produto Específico
```
http://localhost:8080/api/products/1
```
**Resposta:** Samsung Galaxy S24 Ultra

### Teste 3: Comparar Produtos
```
http://localhost:8080/api/products/compare?ids=1,2,3
```
**Resposta:** Array com 3 produtos (Samsung, iPhone, Pixel)

### Teste 4: Análise Inteligente (Recurso Principal) ⭐
```
http://localhost:8080/api/products/compare/analysis?ids=1,2,3
```
**Resposta:** Análise completa com:
- Vencedores por categoria (Preço, Avaliação, Bateria, etc.)
- Pontuação total (0-100)
- Pontos fortes e fracos
- Recomendação personalizada

---

## 🧪 Executar Testes Automatizados

### Via Maven
```bash
.\mvnw.cmd test
```

### Via IDE
1. Clique com botão direito na pasta `src/test/java`
2. Selecione **"Run All Tests"**

**Resultado esperado:**
```
Tests run: 60, Failures: 0, Errors: 0, Skipped: 0
✅ 100% de sucesso
```

---

## ⏹️ Parar a Aplicação

- **IDE:** Clique no botão **Stop** (⏹️)
- **Terminal:** Pressione `Ctrl + C`

---

## 📚 Documentação Adicional

- **README.md** - Visão geral do projeto e arquitetura
- **prompts.md** - Prompts de IA utilizados no desenvolvimento
- **Swagger UI** - Documentação interativa em `http://localhost:8080/swagger-ui.html`

---

## ❓ Troubleshooting

### Erro: "Port 8080 already in use"
**Solução:** Outra aplicação está usando a porta 8080
```bash
# Windows - Encontrar processo na porta 8080
netstat -ano | findstr :8080

# Matar processo (substitua PID)
taskkill /PID <PID> /F
```

### Erro: "Java version not compatible"
**Solução:** Certifique-se de ter Java 21 ou superior
```bash
java -version
```

### Erro: "Dependencies not downloaded"
**Solução:** Force o download das dependências
```bash
.\mvnw.cmd clean install
```

---

**✅ Pronto!** Agora você pode explorar todos os recursos da API de Comparação de Produtos! 🚀
