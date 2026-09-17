package com.ecommerce.checkout.payment;

import com.ecommerce.checkout.domain.entity.Pagamento;
import com.ecommerce.checkout.gateway.ProcessadorPagamento;
import java.util.UUID;

public class PagamentoCartao implements ProcessadorPagamento {
    
    // Simulação: 85% de aprovação para Cartão
    private static final double TAXA_APROVACAO = 0.85;

    @Override
    public boolean processar(Pagamento pagamento) {
        if (pagamento == null) {
            throw new IllegalArgumentException("Pagamento não pode ser null");
        }

        pagamento.marcarComoProcessando();
        
        // Simulação de processamento com gateway de cartão
        boolean aprovado = Math.random() < TAXA_APROVACAO;
        
        if (aprovado) {
            String numeroAutorizacao = gerarNumeroAutorizacao();
            pagamento.marcarComoaprovado(numeroAutorizacao);
            System.out.println("✓ Cartão APROVADO - Autorização: " + numeroAutorizacao);
            return true;
        } else {
            pagamento.marcarComoRecusado();
            System.out.println("✗ Cartão RECUSADO - Verifique seus dados");
            return false;
        }
    }

    @Override
    public String obterMetodoPagamento() {
        return "Cartão de Crédito";
    }

    private String gerarNumeroAutorizacao() {
        return "CARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
