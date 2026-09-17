package com.ecommerce.checkout.domain.exception;

public class PagamentoInvalidoException extends RuntimeException {
    
    public PagamentoInvalidoException(String mensagem) {
        super(mensagem);
    }

    public PagamentoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
