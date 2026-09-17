package com.ecommerce.checkout.gateway;

import com.ecommerce.checkout.domain.entity.Pedido;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RepositorioPedidosEmMemoria implements RepositorioPedidos {
    
    private final Map<String, Pedido> armazenamento = new HashMap<>();

    @Override
    public void salvar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser null");
        }
        armazenamento.put(pedido.getId(), pedido);
    }

    @Override
    public Optional<Pedido> obterPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID do pedido não pode ser null ou vazio");
        }
        return Optional.ofNullable(armazenamento.get(id));
    }

    @Override
    public boolean existe(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID do pedido não pode ser null ou vazio");
        }
        return armazenamento.containsKey(id);
    }
}
