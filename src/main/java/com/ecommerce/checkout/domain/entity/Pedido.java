package com.ecommerce.checkout.domain.entity;

import com.ecommerce.checkout.domain.exception.PedidoInvalidoException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    
    public enum StatusPedido {
        RASCUNHO, CONFIRMADO, PAGAMENTO_PENDENTE, PAGAMENTO_APROVADO, CANCELADO, ENTREGUE
    }

    private final String id;
    private final Cliente cliente;
    private final LocalDateTime dataCriacao;
    private final List<ItemPedido> itens;
    private Pagamento pagamento;
    private StatusPedido status;

    public Pedido(String id, Cliente cliente) {
        validar(id, cliente);
        this.id = id;
        this.cliente = cliente;
        this.dataCriacao = LocalDateTime.now();
        this.itens = new ArrayList<>();
        this.status = StatusPedido.RASCUNHO;
    }

    private void validar(String id, Cliente cliente) {
        if (id == null || id.isBlank()) {
            throw new PedidoInvalidoException("ID do pedido não pode estar vazio");
        }
        if (cliente == null) {
            throw new PedidoInvalidoException("Pedido deve estar associado a um cliente");
        }
    }

    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new PedidoInvalidoException("Item do pedido não pode ser nulo");
        }
        if (status != StatusPedido.RASCUNHO) {
            throw new PedidoInvalidoException("Não é possível adicionar itens a pedido já confirmado");
        }
        itens.add(item);
    }

    public void associarPagamento(Pagamento pagamento) {
        if (pagamento == null) {
            throw new PedidoInvalidoException("Pagamento não pode ser nulo");
        }
        this.pagamento = pagamento;
        this.status = StatusPedido.PAGAMENTO_PENDENTE;
    }

    public void confirmarPagamento() {
        if (pagamento == null || !pagamento.estaAprovado()) {
            throw new PedidoInvalidoException("Pagamento não está aprovado");
        }
        this.status = StatusPedido.PAGAMENTO_APROVADO;
    }

    public void confirmarPedido() {
        if (itens.isEmpty()) {
            throw new PedidoInvalidoException("Pedido deve conter pelo menos um item");
        }
        this.status = StatusPedido.CONFIRMADO;
    }

    public BigDecimal calcularTotal() {
        return itens.stream()
                .map(ItemPedido::calcularTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cancelar() {
        this.status = StatusPedido.CANCELADO;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public boolean temPagamentoAprovado() {
        return pagamento != null && pagamento.estaAprovado();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", cliente=" + cliente +
                ", status=" + status +
                ", total=" + calcularTotal() +
                ", itens=" + itens.size() +
                '}';
    }
}
