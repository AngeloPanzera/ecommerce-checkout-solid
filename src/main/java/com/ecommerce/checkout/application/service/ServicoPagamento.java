package com.ecommerce.checkout.application.service;

import com.ecommerce.checkout.domain.entity.Pagamento;
import com.ecommerce.checkout.domain.exception.PagamentoInvalidoException;
import com.ecommerce.checkout.gateway.ProcessadorPagamento;

public class ServicoPagamento {
    
    private final ProcessadorPagamento processadorPagamento;

    public ServicoPagamento(ProcessadorPagamento processadorPagamento) {
        if (processadorPagamento == null) {
            throw new IllegalArgumentException("Processador de pagamento não pode ser null");
        }
        this.processadorPagamento = processadorPagamento;
    }

    public boolean processar(Pagamento pagamento) {
        if (pagamento == null) {
            throw new PagamentoInvalidoException("Pagamento não pode ser null");
        }

        try {
            return processadorPagamento.processar(pagamento);
        } catch (Exception e) {
            throw new PagamentoInvalidoException(
                "Erro ao processar pagamento: " + e.getMessage(), e
            );
        }
    }

    public String obterMetodoPagamento() {
        return processadorPagamento.obterMetodoPagamento();
    }
}
