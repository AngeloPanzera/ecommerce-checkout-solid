package com.ecommerce.checkout.domain.exception;

public class PedidoInvalidoException extends RuntimeException {
    
    public PedidoInvalidoException(String mensagem) {
        super(mensagem);
    }

    public PedidoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
