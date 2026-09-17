package com.ecommerce.checkout.domain.entity;

import com.ecommerce.checkout.domain.exception.PagamentoInvalidoException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {
    
    public enum StatusPagamento {
        PENDENTE, PROCESSANDO, APROVADO, RECUSADO, CANCELADO
    }

    private final String id;
    private final BigDecimal valor;
    private final String metodo;
    private StatusPagamento status;
    private final LocalDateTime dataCriacao;
    private LocalDateTime dataProcessamento;
    private String autorizacao;

    public Pagamento(String id, BigDecimal valor, String metodo) {
        validar(id, valor, metodo);
        this.id = id;
        this.valor = valor;
        this.metodo = metodo;
        this.status = StatusPagamento.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    private void validar(String id, BigDecimal valor, String metodo) {
        if (id == null || id.isBlank()) {
            throw new PagamentoInvalidoException("ID do pagamento não pode estar vazio");
        }
        if (valor == null || valor.signum() <= 0) {
            throw new PagamentoInvalidoException("Valor do pagamento deve ser maior que zero");
        }
        if (metodo == null || metodo.isBlank()) {
            throw new PagamentoInvalidoException("Método de pagamento não pode estar vazio");
        }
    }

    public void marcarComoaprovado(String numeroAutorizacao) {
        this.status = StatusPagamento.APROVADO;
        this.autorizacao = numeroAutorizacao;
        this.dataProcessamento = LocalDateTime.now();
    }

    public void marcarComoRecusado() {
        this.status = StatusPagamento.RECUSADO;
        this.dataProcessamento = LocalDateTime.now();
    }

    public void marcarComoProcessando() {
        this.status = StatusPagamento.PROCESSANDO;
    }

    // Getters
    public String getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getMetodo() {
        return metodo;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataProcessamento() {
        return dataProcessamento;
    }

    public String getAutorizacao() {
        return autorizacao;
    }

    public boolean estaAprovado() {
        return status == StatusPagamento.APROVADO;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id='" + id + '\'' +
                ", valor=" + valor +
                ", metodo='" + metodo + '\'' +
                ", status=" + status +
                '}';
    }
}
