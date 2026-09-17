package com.ecommerce.checkout.gateway;

import com.ecommerce.checkout.domain.entity.Pagamento;

public interface ProcessadorPagamento {

    boolean processar(Pagamento pagamento);

    String obterMetodoPagamento();
}
