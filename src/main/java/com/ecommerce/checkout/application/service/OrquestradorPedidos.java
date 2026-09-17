package com.ecommerce.checkout.application.service;

import com.ecommerce.checkout.domain.entity.Pedido;
import com.ecommerce.checkout.domain.entity.Pagamento;
import com.ecommerce.checkout.domain.exception.PedidoInvalidoException;
import com.ecommerce.checkout.gateway.RepositorioPedidos;
import com.ecommerce.checkout.notification.CanalNotificacao;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class OrquestradorPedidos {
    
    private final RepositorioPedidos repositorioPedidos;
    private final List<CanalNotificacao> canaisNotificacao;

    public OrquestradorPedidos(
            RepositorioPedidos repositorioPedidos,
            List<CanalNotificacao> canaisNotificacao) {
        
        if (repositorioPedidos == null) {
            throw new IllegalArgumentException("Repositório de pedidos não pode ser null");
        }
        if (canaisNotificacao == null || canaisNotificacao.isEmpty()) {
            throw new IllegalArgumentException("Deve haver pelo menos um canal de notificação");
        }
        
        this.repositorioPedidos = repositorioPedidos;
        this.canaisNotificacao = new ArrayList<>(canaisNotificacao);
    }

    public boolean executarCheckout(Pedido pedido, ServicoPagamento servicoPagamento) {
        if (pedido == null) {
            throw new PedidoInvalidoException("Pedido não pode ser null");
        }
        if (servicoPagamento == null) {
            throw new PedidoInvalidoException("Serviço de pagamento não pode ser null");
        }

        try {
            // 1. Confirmar pedido
            pedido.confirmarPedido();
            System.out.println("\n🔔 Pedido confirmado: " + pedido.getId());

            // 2. Criar pagamento (o valor vem do pedido)
            Pagamento pagamento = new Pagamento(
                gerarIdPagamento(),
                pedido.calcularTotal(),
                servicoPagamento.obterMetodoPagamento()
            );

            // 3. Associar pagamento ao pedido
            pedido.associarPagamento(pagamento);

            // 4. Processar pagamento (uso da estratégia injetada)
            System.out.println("\n💳 Processando pagamento via " + servicoPagamento.obterMetodoPagamento());
            boolean pagamentoAprovado = servicoPagamento.processar(pagamento);

            if (pagamentoAprovado) {
                // 5a. Confirmar pagamento no pedido
                pedido.confirmarPagamento();
                System.out.println("✓ Pagamento confirmado no pedido");

                // 6a. Notificar sucesso através de TODOS os canais
                notificarPorTodosCanais(pedido, TipoNotificacao.PAGAMENTO_APROVADO);

                // 7a. Salvar pedido (transição de estado final)
                repositorioPedidos.salvar(pedido);
                System.out.println("✓ Pedido salvo com sucesso");

                return true;
            } else {
                // 5b. Notificar falha através de TODOS os canais
                notificarPorTodosCanais(pedido, TipoNotificacao.FALHA_PAGAMENTO);

                // 6b. Salvar pedido em estado de falha
                repositorioPedidos.salvar(pedido);
                System.out.println("✗ Pedido salvo com falha de pagamento");

                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Erro no checkout: " + e.getMessage());
            throw new PedidoInvalidoException("Erro ao executar checkout", e);
        }
    }

    public Pedido obterPedido(String idPedido) {
        if (idPedido == null || idPedido.isBlank()) {
            throw new PedidoInvalidoException("ID do pedido não pode ser vazio");
        }

        return repositorioPedidos.obterPorId(idPedido)
                .orElseThrow(() -> new PedidoInvalidoException("Pedido não encontrado: " + idPedido));
    }

    private void notificarPorTodosCanais(Pedido pedido, TipoNotificacao tipo) {
        System.out.println("\n📢 Enviando notificações...");
        
        for (CanalNotificacao canal : canaisNotificacao) {
            try {
                switch (tipo) {
                    case CONFIRMACAO:
                        canal.notificarConfirmacao(pedido);
                        break;
                    case PAGAMENTO_APROVADO:
                        canal.notificarPagamentoAprovado(pedido);
                        break;
                    case FALHA_PAGAMENTO:
                        canal.notificarFalhaPagamento(pedido);
                        break;
                }
            } catch (Exception e) {
                System.err.println("⚠️ Falha ao notificar via " + canal.obterCanal() + ": " + e.getMessage());
            }
        }
    }

    private String gerarIdPagamento() {
        return "PAG-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    private enum TipoNotificacao {
        CONFIRMACAO, PAGAMENTO_APROVADO, FALHA_PAGAMENTO
    }
}
