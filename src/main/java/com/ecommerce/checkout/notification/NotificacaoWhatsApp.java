package com.ecommerce.checkout.notification;

import com.ecommerce.checkout.domain.entity.Pedido;
import java.time.format.DateTimeFormatter;

public class NotificacaoWhatsApp implements CanalNotificacao {

    @Override
    public void notificarConfirmacao(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser null");
        }
        
        String para = pedido.getCliente().getTelefone();
        String mensagem = gerarMensagemConfirmacao(pedido);
        
        enviarMensagem(para, mensagem);
    }

    @Override
    public void notificarPagamentoAprovado(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser null");
        }
        
        String para = pedido.getCliente().getTelefone();
        String mensagem = gerarMensagemPagamentoAprovado(pedido);
        
        enviarMensagem(para, mensagem);
    }

    @Override
    public void notificarFalhaPagamento(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser null");
        }
        
        String para = pedido.getCliente().getTelefone();
        String mensagem = gerarMensagemFalhaPagamento(pedido);
        
        enviarMensagem(para, mensagem);
    }

    @Override
    public String obterCanal() {
        return "WhatsApp";
    }

    private void enviarMensagem(String para, String mensagem) {
        System.out.println("\n💬 WHATSAPP ENVIADO");
        System.out.println("  Para: " + para);
        System.out.println("  Mensagem: " + mensagem);
    }

    private String gerarMensagemConfirmacao(Pedido pedido) {
        return "Olá " + pedido.getCliente().getNome() + "! 👋\n" +
               "Seu pedido #" + pedido.getId() + " foi confirmado! ✅\n" +
               "Total: R$ " + pedido.calcularTotal() + "\n" +
               "Data: " + formatarData(pedido.getDataCriacao()) + "\n\n" +
               "Aguardamos seu pagamento! 💳";
    }

    private String gerarMensagemPagamentoAprovado(Pedido pedido) {
        return "Ótimas notícias " + pedido.getCliente().getNome() + "! 🎉\n" +
               "Seu pagamento foi APROVADO! ✅\n" +
               "Pedido: #" + pedido.getId() + "\n" +
               "Valor: R$ " + pedido.getPagamento().getValor() + "\n\n" +
               "Seu pedido será processado em breve! 📦";
    }

    private String gerarMensagemFalhaPagamento(Pedido pedido) {
        return "Opa! Houve um problema 😕\n" +
               "Seu pagamento foi recusado.\n" +
               "Pedido: #" + pedido.getId() + "\n\n" +
               "Tente novamente com outro método! 💳";
    }

    private String formatarData(java.time.LocalDateTime data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
