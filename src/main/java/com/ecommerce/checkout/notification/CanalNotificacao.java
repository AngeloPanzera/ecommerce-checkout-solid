package com.ecommerce.checkout.notification;

import com.ecommerce.checkout.domain.entity.Pedido;

public interface CanalNotificacao {
    
    /**
     * Envia uma notificação de confirmação de pedido.
     * [LSP] Todas as implementações devem garantir entrega da notificação
     * sem lançar exceções não documentadas.
     * 
     * @param pedido O pedido a notificar
     * @throws IllegalArgumentException se o pedido for null
     */
    void notificarConfirmacao(Pedido pedido);
    
    /**
     * Envia uma notificação de pagamento aprovado.
     * 
     * @param pedido O pedido com pagamento aprovado
     * @throws IllegalArgumentException se o pedido for null
     */
    void notificarPagamentoAprovado(Pedido pedido);
    
    /**
     * Envia uma notificação de falha de pagamento.
     * 
     * @param pedido O pedido cuja transação falhou
     * @throws IllegalArgumentException se o pedido for null
     */
    void notificarFalhaPagamento(Pedido pedido);
    
    /**
     * Retorna o nome/tipo do canal de notificação.
     * 
     * @return Nome do canal (ex: "Email", "WhatsApp", "SMS")
     */
    String obterCanal();
}
