package com.ecommerce.checkout.gateway;

import com.ecommerce.checkout.domain.entity.Pedido;
import java.util.Optional;

public interface RepositorioPedidos {

    void salvar(Pedido pedido);

    Optional<Pedido> obterPorId(String id);

    boolean existe(String id);
}
