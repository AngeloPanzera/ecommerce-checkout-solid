# 🛒 Sistema de Checkout E-commerce - Aplicação SOLID em Java Puro

## 📋 Resumo Executivo

Projeto acadêmico completo em **Java Puro** que demonstra a conformidade estrita com todos os **5 princípios SOLID** através de um **Motor de Checkout de E-commerce** profissional.

### ✅ Conformidade SOLID Garantida

| Princípio | Status | Demonstração |
|-----------|--------|--------------|
| **S** Single Responsibility | ✓ | Cada classe tem UMA responsabilidade clara |
| **O** Open/Closed | ✓ | Aberto para extensão (novos pagamentos/canais), fechado para modificação |
| **L** Liskov Substitution | ✓ | Todas as implementações cumprem o contrato sem exceções inesperadas |
| **I** Interface Segregation | ✓ | Interfaces específicas (1-3 métodos), nenhuma classe forçada a depender de métodos não utilizados |
| **D** Dependency Inversion | ✓ | Serviços dependem apenas de abstrações (interfaces), injeção manual via construtor |

---

## 🏗️ Arquitetura

```
ecommerce-checkout-solid/
├── src/main/java/
│   └── com/ecommerce/checkout/
│       ├── domain/
│       │   ├── entity/
│       │   │   ├── Pedido.java           (Agregado raiz)
│       │   │   ├── ItemPedido.java       (Entidade de valor)
│       │   │   ├── Cliente.java          (Entidade)
│       │   │   └── Pagamento.java        (Entidade)
│       │   └── exception/
│       │       ├── PagamentoInvalidoException.java
│       │       └── PedidoInvalidoException.java
│       │
│       ├── application/
│       │   └── service/
│       │       ├── OrquestradorPedidos.java   (Controladora - Serviço de App)
│       │       └── ServicoPagamento.java      (Serviço de Domínio)
│       │
│       ├── gateway/
│       │   ├── ProcessadorPagamento.java      (Interface - ISP)
│       │   ├── RepositorioPedidos.java        (Interface - ISP, DIP)
│       │   └── RepositorioPedidosEmMemoria.java (Implementação)
│       │
│       ├── payment/
│       │   ├── PagamentoPix.java              (Implementação)
│       │   ├── PagamentoCartao.java           (Implementação)
│       │   └── PagamentoBoleto.java           (Implementação)
│       │
│       ├── notification/
│       │   ├── CanalNotificacao.java          (Interface - ISP)
│       │   ├── NotificacaoEmail.java          (Implementação)
│       │   └── NotificacaoWhatsApp.java       (Implementação)
│       │
│       └── Main.java                          (Ponto de entrada)
│
├── .gitignore                                 (Configuração Git)
└── README.md                                  (Este arquivo)
```

---

## 📦 Entidades de Domínio (4+ classes obrigatórias)

### 1️⃣ **Pedido.java** (Agregado Raiz)
- **SRP**: Encapsula dados do pedido, gerencia itens e invariantes de estado
- **Responsabilidade única**: Representar e validar um pedido
- Invariantes: Pedido sempre tem ≥1 item e 1 cliente válido

```java
Pedido pedido = new Pedido("PED-001", cliente);
pedido.adicionarItem(new ItemPedido("SKU-001", "Produto", 1, BigDecimal.TEN));
pedido.confirmarPedido();
```

### 2️⃣ **ItemPedido.java**
- **SRP**: Representa um item do pedido com suas propriedades
- Validações internas: Quantidade > 0, Preço > 0
- Cálculo do total do item encapsulado

### 3️⃣ **Cliente.java**
- **SRP**: Encapsula dados do cliente
- Validações: Email válido, CPF com 11 dígitos
- Imutável após criação

### 4️⃣ **Pagamento.java**
- **SRP**: Representa informações de pagamento processado
- Estados: PENDENTE → PROCESSANDO → APROVADO/RECUSADO
- Validações: Valor > 0, Método não vazio

---

## 🔌 Abstrações (3+ interfaces obrigatórias)

### Interface 1: **ProcessadorPagamento** (ISP)
```java
public interface ProcessadorPagamento {
    boolean processar(Pagamento pagamento);
    String obterMetodoPagamento();
}
```

**Implementações**:
- ✓ `PagamentoPix` (95% aprovação)
- ✓ `PagamentoCartao` (85% aprovação)
- ✓ `PagamentoBoleto` (100% geração)

**Princípios**:
- **[OCP]** Novas formas de pagamento sem modificar código existente
- **[LSP]** Todas cumprem o contrato sem `UnsupportedOperationException`
- **[ISP]** Interface pequena e coesa (2 métodos)

### Interface 2: **CanalNotificacao** (ISP)
```java
public interface CanalNotificacao {
    void notificarConfirmacao(Pedido pedido);
    void notificarPagamentoAprovado(Pedido pedido);
    void notificarFalhaPagamento(Pedido pedido);
    String obterCanal();
}
```

**Implementações**:
- ✓ `NotificacaoEmail` (Formato HTML)
- ✓ `NotificacaoWhatsApp` (Formato com emojis)

**Princípios**:
- **[OCP]** Novos canais (SMS, Telegram, etc) sem modificação
- **[LSP]** Todas garantem entrega
- **[ISP]** Interface específica para notificações

### Interface 3: **RepositorioPedidos** (DIP)
```java
public interface RepositorioPedidos {
    void salvar(Pedido pedido);
    Optional<Pedido> obterPorId(String id);
    boolean existe(String id);
}
```

**Implementação**:
- ✓ `RepositorioPedidosEmMemoria`

**Princípios**:
- **[DIP]** Alta-nível depende de abstração, não de impl. concreta
- **[LSP]** Cumpre contrato sem exceções inesperadas

---

## 🎯 Serviços/Controladoras (2+ obrigatórios)

### 1️⃣ **OrquestradorPedidos** (Serviço de Aplicação)

**Responsabilidade única**:
- Orquestrar o fluxo completo de checkout
- Não viola encapsulamento das entidades

**Conformidade SOLID**:
```java
✓ [SRP] Única responsabilidade: coordenar fluxo
✓ [OCP] Suporta novos métodos de pagamento e canais SEM modificação
✓ [DIP] Depende de ProcessadorPagamento, RepositorioPedidos, CanalNotificacao (interfaces)
✓ [LSP] Delega a operações polimórficas sem casting
✓ [ISP] Usa apenas métodos necessários de cada interface
```

**Fluxo de Checkout**:
```
1. Confirmar pedido
2. Criar pagamento
3. Processar pagamento (via ProcessadorPagamento injetado)
4. Se aprovado:
   - Confirmar pagamento no pedido
   - Notificar sucesso (TODOS os canais)
   - Salvar no repositório
5. Se recusado:
   - Notificar falha
   - Salvar em estado de falha
```

### 2️⃣ **ServicoPagamento** (Serviço de Domínio)

**Responsabilidade única**:
- Orquestração da lógica de processamento de pagamentos

**Conformidade SOLID**:
```java
✓ [DIP] Recebe ProcessadorPagamento via construtor (não usa 'new')
✓ [SRP] Única responsabilidade: delegar ao processador correto
✓ [LSP] Não força implementação a fazer mais do que pode
```

---

## 🚀 Execução e Cenários

### Como Compilar

```bash
# Navegar até a pasta do projeto
cd ecommerce-checkout-solid

# Compilar classes
javac -d out/production/src $(find src -name "*.java")

# Executar
java -cp out/production/src com.ecommerce.checkout.Main
```

### Cenários Demonstrados

#### **Cenário 1: Pedido com Pix**
```
Cliente: João Silva
Método: Pix
Notificações: Email + WhatsApp

Itens:
  - Notebook Gamer 15" (R$ 3.500,00)
  - Mouse Wireless x2 (R$ 150,00 cada)

Total: R$ 3.800,00
```

#### **Cenário 2: Pedido com Cartão**
```
Cliente: Maria Santos
Método: Cartão de Crédito
Notificações: Email + WhatsApp

Itens:
  - Monitor 27" 144Hz (R$ 1.200,00)
  - Teclado Mecânico RGB (R$ 450,00)
  - Cabo HDMI 2.1 x3 (R$ 80,00 cada)

Total: R$ 1.890,00
```

---

## 🛡️ Garantias SOLID

### ✓ Single Responsibility Principle (SRP)

Cada classe tem **UMA única responsabilidade**:

| Classe | Responsabilidade |
|--------|-----------------|
| `Pedido` | Encapsular dados e invariantes do pedido |
| `ItemPedido` | Representar um item com validações |
| `Pagamento` | Gerenciar estado e dados de pagamento |
| `Cliente` | Encapsular dados do cliente |
| `OrquestradorPedidos` | Coordenar fluxo de checkout |
| `ServicoPagamento` | Delegar ao processador correto |
| `PagamentoPix` | Processar pagamento via Pix |
| `NotificacaoEmail` | Enviar notificações por email |

### ✓ Open/Closed Principle (OCP)

**ABERTO** para extensão, **FECHADO** para modificação:

```java
// ✓ Adicionar novo método de pagamento:
public class PagamentoApplePay implements ProcessadorPagamento {
    public boolean processar(Pagamento pag) { /* ... */ }
    public String obterMetodoPagamento() { return "Apple Pay"; }
}

// Usar no Main:
ProcessadorPagamento apple = new PagamentoApplePay();
ServicoPagamento servico = new ServicoPagamento(apple);
// ✓ SEM MODIFICAR OrquestradorPedidos ou nenhuma classe existente!
```

**Nenhuma instrução**:
```java
// ✗ PROIBIDO (violaria OCP):
if (pagamento instanceof PagamentoPix) { ... }
switch(tipoMetodo) { case "PIX": ... }
```

### ✓ Liskov Substitution Principle (LSP)

Todas as implementações cumprem o contrato:

```java
// ✓ Qualquer ProcessadorPagamento pode substituir outro:
ProcessadorPagamento p1 = new PagamentoPix();
ProcessadorPagamento p2 = new PagamentoCartao();
ProcessadorPagamento p3 = new PagamentoBoleto();

ServicoPagamento s1 = new ServicoPagamento(p1); // Funciona!
ServicoPagamento s2 = new ServicoPagamento(p2); // Funciona!
ServicoPagamento s3 = new ServicoPagamento(p3); // Funciona!

// ✗ PROIBIDO:
public class PagamentoInvalido implements ProcessadorPagamento {
    public boolean processar(Pagamento p) {
        throw new UnsupportedOperationException(); // ✗ VIOLA LSP!
    }
}
```

### ✓ Interface Segregation Principle (ISP)

Interfaces **pequenas e específicas**, ninguém é forçado a depender de métodos não utilizados:

```java
// ✓ Interface segregada (3 métodos)
public interface CanalNotificacao {
    void notificarConfirmacao(Pedido pedido);
    void notificarPagamentoAprovado(Pedido pedido);
    void notificarFalhaPagamento(Pedido pedido);
}

// ✗ Interface gorda (violaria ISP):
public interface Notificador {
    void notificar(Pedido p);
    void notificarEmail(Pedido p);
    void notificarSMS(Pedido p);
    void notificarWhatsApp(Pedido p);
    void notificarTelegram(Pedido p);
    void logs(String msg);
    void salvarBD();
    // ... muitos outros métodos não relacionados
}
```

### ✓ Dependency Inversion Principle (DIP)

Alto-nível depende de abstrações, não de implementações:

```java
// ✓ DIP: Depende de interface (abstração)
public OrquestradorPedidos(
    RepositorioPedidos repo,           // ← Interface
    List<CanalNotificacao> canais) {   // ← Interface
    this.repositorio = repo;
    this.canais = canais;
}

// ✗ NÃO usar 'new' para instanciar dependências:
// PROIBIDO no serviço:
public OrquestradorPedidos() {
    this.repositorio = new RepositorioPedidosEmMemoria(); // ✗ Acoplamento!
    this.processador = new PagamentoPix(); // ✗ Acoplamento!
}
```

---

## 📊 Análise de Requisitos

### ✅ Requisito 1: Modelagem Estrutural

- **Mínimo 4 classes de domínio**: ✓ Pedido, ItemPedido, Cliente, Pagamento
- **Mínimo 3 interfaces**: ✓ ProcessadorPagamento, CanalNotificacao, RepositorioPedidos
- **Mínimo 2 controladoras**: ✓ OrquestradorPedidos, ServicoPagamento
- **Sem exemplos acadêmicos**: ✓ Motor real de E-commerce

### ✅ Requisito 2: Conformidade SOLID

- **[S]** Cada classe tem responsabilidade única: ✓
- **[O]** Aberto para extensão, fechado para modificação: ✓
- **[L]** Sem UnsupportedOperationException: ✓
- **[I]** Interfaces pequenas e segregadas: ✓
- **[D]** Injeção manual de dependências: ✓

### ✅ Requisito 3: Execução

- **Ponto de entrada (Main.java)**: ✓
- **Grafo de objetos montado**: ✓
- **Dois cenários distintos**: ✓ Pix + Cartão

### ✅ Requisito 4: Repositório GitHub

- **Código organizado**: ✓ src/main/java com padrão
- **.gitignore**: ✓ Ignora target/, .idea/, .class
- **Link pronto para submissão**: [Seu repositório aqui]

---

## 🧪 Testes de Extensão

### Adicionar novo método de pagamento

```java
// 1. Criar nova classe (SEM modificar nada existente):
public class PagamentoTransferencia implements ProcessadorPagamento {
    @Override
    public boolean processar(Pagamento pagamento) {
        // Implementação específica
        pagamento.marcarComoaprovado("TRF-" + UUID.randomUUID());
        return true;
    }
    
    @Override
    public String obterMetodoPagamento() {
        return "Transferência Bancária";
    }
}

// 2. Usar no Main (sem modificar OrquestradorPedidos):
ProcessadorPagamento processador = new PagamentoTransferencia();
ServicoPagamento servico = new ServicoPagamento(processador);
orquestrador.executarCheckout(pedido, servico);
```

### Adicionar novo canal de notificação

```java
// 1. Criar nova classe:
public class NotificacaoSMS implements CanalNotificacao {
    @Override
    public void notificarConfirmacao(Pedido pedido) { /* ... */ }
    @Override
    public void notificarPagamentoAprovado(Pedido pedido) { /* ... */ }
    @Override
    public void notificarFalhaPagamento(Pedido pedido) { /* ... */ }
    @Override
    public String obterCanal() { return "SMS"; }
}

// 2. Adicionar no Main:
List<CanalNotificacao> canais = Arrays.asList(
    new NotificacaoEmail(),
    new NotificacaoWhatsApp(),
    new NotificacaoSMS()  // ← Novo, sem modificação!
);
OrquestradorPedidos orquestrador = new OrquestradorPedidos(repo, canais);
```

---

## 📚 Estrutura de Pastas do GitHub

```
ecommerce-checkout-solid/
├── .gitignore
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/ecommerce/checkout/
│               ├── domain/
│               ├── application/
│               ├── gateway/
│               ├── payment/
│               ├── notification/
│               └── Main.java
└── out/  (gerado na compilação, ignorado por .gitignore)
```

---

## 🎓 Justificativa Técnica

### Por que não usar um Framework de DI?

Este projeto utiliza **injeção manual de dependências** (sem Spring, Guice, etc.) para:

1. **Demonstrar entendimento real** dos princípios SOLID
2. **Evitar "magia" de frameworks** que obscurece o aprendizado
3. **Mostrar controle explícito** sobre o grafo de objetos
4. **Facilitar defesa e arguição técnica**

### Por que usar interfaces pequenas?

```java
// ✓ ISP: Interface pequena
public interface ProcessadorPagamento {
    boolean processar(Pagamento pag);
    String obterMetodoPagamento();
}

// Ninguém é forçado a depender de:
public interface GigantePagamento {
    boolean processar(Pagamento pag);
    String obterMetodoPagamento();
    void salvarBD();
    void enviarRelatorio();
    void validarFrau...

    // ... 20+ métodos não relacionados
}
```

---

## 🚀 Como Preparar para Defesa

### Pontos de Apoio:

1. **Single Responsibility**: "Veja que Pedido gerencia APENAS dados do pedido, não pagamento ou notificação"

2. **Open/Closed**: "Para adicionar novo método de pagamento, criamos PagamentoTransferencia SEM modificar nada. Se houvesse switch/case, teríamos que modificar."

3. **Liskov Substitution**: "Qualquer ProcessadorPagamento pode substituir outro. Nenhuma delas lança UnsupportedOperationException."

4. **Interface Segregation**: "ProcessadorPagamento tem apenas 2 métodos. Ninguém é forçado a depender de métodos não utilizados."

5. **Dependency Inversion**: "OrquestradorPedidos recebe as interfaces via construtor. Nunca usa 'new' para criar dependências."

---

## 📝 Licença

Projeto acadêmico - Uso livre para fins educacionais.

---

**Autor**: Projeto SOLID em Java  
**Data**: 2026  
**Versão**: 1.0

