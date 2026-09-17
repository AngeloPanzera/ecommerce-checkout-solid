package com.ecommerce.checkout;

import com.ecommerce.checkout.application.service.OrquestradorPedidos;
import com.ecommerce.checkout.application.service.ServicoPagamento;
import com.ecommerce.checkout.domain.entity.Cliente;
import com.ecommerce.checkout.domain.entity.ItemPedido;
import com.ecommerce.checkout.domain.entity.Pedido;
import com.ecommerce.checkout.gateway.RepositorioPedidos;
import com.ecommerce.checkout.gateway.RepositorioPedidosEmMemoria;
import com.ecommerce.checkout.gateway.ProcessadorPagamento;
import com.ecommerce.checkout.notification.CanalNotificacao;
import com.ecommerce.checkout.payment.PagamentoPix;
import com.ecommerce.checkout.payment.PagamentoCartao;
import com.ecommerce.checkout.notification.NotificacaoEmail;
import com.ecommerce.checkout.notification.NotificacaoWhatsApp;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE CHECKOUT E-COMMERCE - PRINCÍPIOS SOLID     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        // ═══════════════════════════════════════════════════════════════════════════
        // MONTAGEM DO GRAFO DE OBJETOS (Dependency Injection Manual)
        // ═══════════════════════════════════════════════════════════════════════════

        // 1. Criar repositório (implementação concreta)
        RepositorioPedidos repositorio = new RepositorioPedidosEmMemoria();

        // 2. Criar canais de notificação (implementações concretas)
        CanalNotificacao email = new NotificacaoEmail();
        CanalNotificacao whatsapp = new NotificacaoWhatsApp();

        // 3. Criar orquestrador com dependências injetadas
        OrquestradorPedidos orquestrador = new OrquestradorPedidos(
            repositorio,
            Arrays.asList(email, whatsapp)  // [OCP] Fácil adicionar novos canais
        );

        // ═══════════════════════════════════════════════════════════════════════════
        // CENÁRIO 1: Pedido com Pagamento via Pix + Notificações Email e WhatsApp
        // ═══════════════════════════════════════════════════════════════════════════
        System.out.println("\n" + "═".repeat(60));
        System.out.println("CENÁRIO 1: Pedido com PIX");
        System.out.println("═".repeat(60));

        // Dados do cliente
        Cliente cliente1 = new Cliente(
            "CLI-" + UUID.randomUUID().toString().substring(0, 8),
            "João Silva",
            "joao.silva@email.com",
            "11999999999",
            "12345678901"
        );

        // Criar pedido
        Pedido pedido1 = new Pedido(
            "PED-" + UUID.randomUUID().toString().substring(0, 8),
            cliente1
        );

        // Adicionar itens
        pedido1.adicionarItem(new ItemPedido(
            "SKU-001",
            "Notebook Gamer 15\"",
            1,
            new BigDecimal("3500.00")
        ));

        pedido1.adicionarItem(new ItemPedido(
            "SKU-002",
            "Mouse Wireless",
            2,
            new BigDecimal("150.00")
        ));

        System.out.println("\n📦 Pedido criado:");
        System.out.println("   " + pedido1);
        System.out.println("   Total: R$ " + pedido1.calcularTotal());
        System.out.println("   Itens: ");
        for (ItemPedido item : pedido1.getItens()) {
            System.out.println("     - " + item);
        }

        // [OCP] Criar serviço de pagamento com Pix (processador específico injetado)
        ProcessadorPagamento processadorPix = new PagamentoPix();
        ServicoPagamento servicoPagamentoPix = new ServicoPagamento(processadorPix);

        // Executar checkout
        System.out.println("\n🛒 Iniciando checkout com Pix...");
        boolean checkoutSucesso1 = orquestrador.executarCheckout(pedido1, servicoPagamentoPix);

        // Resultado
        System.out.println("\n✅ Resultado Cenário 1: " + (checkoutSucesso1 ? "SUCESSO" : "FALHA"));
        System.out.println("   Pedido Status: " + pedido1.getStatus());
        System.out.println("   Pagamento Status: " + pedido1.getPagamento().getStatus());

        // ═══════════════════════════════════════════════════════════════════════════
        // CENÁRIO 2: Pedido com Pagamento via Cartão + Notificações Email e WhatsApp
        // ═══════════════════════════════════════════════════════════════════════════
        System.out.println("\n\n" + "═".repeat(60));
        System.out.println("CENÁRIO 2: Pedido com CARTÃO DE CRÉDITO");
        System.out.println("═".repeat(60));

        // Dados do cliente (diferente)
        Cliente cliente2 = new Cliente(
            "CLI-" + UUID.randomUUID().toString().substring(0, 8),
            "Maria Santos",
            "maria.santos@email.com",
            "21988888888",
            "98765432101"
        );

        // Criar pedido
        Pedido pedido2 = new Pedido(
            "PED-" + UUID.randomUUID().toString().substring(0, 8),
            cliente2
        );

        // Adicionar itens (diferentes)
        pedido2.adicionarItem(new ItemPedido(
            "SKU-003",
            "Monitor 27\" 144Hz",
            1,
            new BigDecimal("1200.00")
        ));

        pedido2.adicionarItem(new ItemPedido(
            "SKU-004",
            "Teclado Mecânico RGB",
            1,
            new BigDecimal("450.00")
        ));

        pedido2.adicionarItem(new ItemPedido(
            "SKU-005",
            "Cabo HDMI 2.1",
            3,
            new BigDecimal("80.00")
        ));

        System.out.println("\n📦 Pedido criado:");
        System.out.println("   " + pedido2);
        System.out.println("   Total: R$ " + pedido2.calcularTotal());
        System.out.println("   Itens: ");
        for (ItemPedido item : pedido2.getItens()) {
            System.out.println("     - " + item);
        }

        // [OCP] Criar serviço de pagamento com Cartão (processador específico injetado)
        ProcessadorPagamento processadorCartao = new PagamentoCartao();
        ServicoPagamento servicoPagamentoCartao = new ServicoPagamento(processadorCartao);

        // Executar checkout
        System.out.println("\n🛒 Iniciando checkout com Cartão de Crédito...");
        boolean checkoutSucesso2 = orquestrador.executarCheckout(pedido2, servicoPagamentoCartao);

        // Resultado
        System.out.println("\n✅ Resultado Cenário 2: " + (checkoutSucesso2 ? "SUCESSO" : "FALHA"));
        System.out.println("   Pedido Status: " + pedido2.getStatus());
        System.out.println("   Pagamento Status: " + pedido2.getPagamento().getStatus());

        // ═══════════════════════════════════════════════════════════════════════════
        // RESUMO FINAL
        // ═══════════════════════════════════════════════════════════════════════════
        System.out.println("\n\n" + "═".repeat(60));
        System.out.println("RESUMO FINAL DA EXECUÇÃO");
        System.out.println("═".repeat(60));
        System.out.println("\n✓ Pedido 1 (PIX):");
        System.out.println("  ID: " + pedido1.getId());
        System.out.println("  Cliente: " + pedido1.getCliente().getNome());
        System.out.println("  Status: " + pedido1.getStatus());
        System.out.println("  Total: R$ " + pedido1.calcularTotal());

        System.out.println("\n✓ Pedido 2 (CARTÃO):");
        System.out.println("  ID: " + pedido2.getId());
        System.out.println("  Cliente: " + pedido2.getCliente().getNome());
        System.out.println("  Status: " + pedido2.getStatus());
        System.out.println("  Total: R$ " + pedido2.calcularTotal());

        System.out.println("\n" + "═".repeat(60));
        System.out.println("✓ APLICAÇÃO ENCERRADA COM SUCESSO");
        System.out.println("═".repeat(60));
    }
}
