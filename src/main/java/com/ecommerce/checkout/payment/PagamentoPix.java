package com.ecommerce.checkout.payment;

import com.ecommerce.checkout.domain.entity.Pagamento;
import com.ecommerce.checkout.gateway.ProcessadorPagamento;
import java.util.UUID;

public class PagamentoPix implements ProcessadorPagamento {
    
    // Simulação: 95% de aprovação para Pix
    private static final double TAXA_APROVACAO = 0.95;

    @Override
    public boolean processar(Pagamento pagamento) {
        if (pagamento == null) {
            throw new IllegalArgumentException("Pagamento não pode ser null");
        }

        pagamento.marcarComoProcessando();
        
        // Simulação de processamento: 95% de aprovação
        boolean aprovado = Math.random() < TAXA_APROVACAO;
        
        if (aprovado) {
            String numeroAutorizacao = gerarNumeroAutorizacao();
            pagamento.marcarComoaprovado(numeroAutorizacao);
            System.out.println("✓ Pix APROVADO - Autorização: " + numeroAutorizacao);
            return true;
        } else {
            pagamento.marcarComoRecusado();
            System.out.println("✗ Pix RECUSADO - Tente novamente");
            return false;
        }
    }

    @Override
    public String obterMetodoPagamento() {
        return "Pix";
    }

    private String gerarNumeroAutorizacao() {
        return "PIX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
