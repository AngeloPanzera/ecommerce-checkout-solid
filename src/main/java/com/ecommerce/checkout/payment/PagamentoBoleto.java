package com.ecommerce.checkout.payment;

import com.ecommerce.checkout.domain.entity.Pagamento;
import com.ecommerce.checkout.gateway.ProcessadorPagamento;
import java.util.UUID;

public class PagamentoBoleto implements ProcessadorPagamento {
    
    // Boletos sempre iniciam como pendentes (100% de "aprovação" = geração do boleto)
    private static final double TAXA_GERACAO = 1.0;

    @Override
    public boolean processar(Pagamento pagamento) {
        if (pagamento == null) {
            throw new IllegalArgumentException("Pagamento não pode ser null");
        }

        pagamento.marcarComoProcessando();
        
        // Boleto sempre é aceito (gera o código)
        boolean aceito = Math.random() < TAXA_GERACAO;
        
        if (aceito) {
            String numeroAutorizacao = gerarCodigoBoleto();
            pagamento.marcarComoaprovado(numeroAutorizacao);
            System.out.println("✓ Boleto GERADO - Código: " + numeroAutorizacao);
            System.out.println("  (Pagável até 3 dias úteis)");
            return true;
        } else {
            pagamento.marcarComoRecusado();
            System.out.println("✗ Falha ao gerar boleto");
            return false;
        }
    }

    @Override
    public String obterMetodoPagamento() {
        return "Boleto Bancário";
    }

    private String gerarCodigoBoleto() {
        // Simula um código de boleto real (banco.agencia.conta)
        return "12345.67890 12345.678901 12345.678901 1 " + System.currentTimeMillis() % 10000000;
    }
}
