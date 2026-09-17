package com.ecommerce.checkout.notification;

import com.ecommerce.checkout.domain.entity.Pedido;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificacaoEmail implements CanalNotificacao {

    @Override
    public void notificarConfirmacao(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser null");
        }
        
        String para = pedido.getCliente().getEmail();
        String assunto = "[Pedido Confirmado] " + pedido.getId();
        String corpo = gerarCorpoConfirmacao(pedido);
        
        enviarEmail(para, assunto, corpo);
    }

    @Override
    public void notificarPagamentoAprovado(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser null");
        }
        
        String para = pedido.getCliente().getEmail();
        String assunto = "[Pagamento Aprovado] Pedido " + pedido.getId();
        String corpo = gerarCorpoPagamentoAprovado(pedido);
        
        enviarEmail(para, assunto, corpo);
    }

    @Override
    public void notificarFalhaPagamento(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser null");
        }
        
        String para = pedido.getCliente().getEmail();
        String assunto = "[Falha de Pagamento] Pedido " + pedido.getId();
        String corpo = gerarCorpoFalhaPagamento(pedido);
        
        enviarEmail(para, assunto, corpo);
    }

    @Override
    public String obterCanal() {
        return "Email";
    }

    private void enviarEmail(String para, String assunto, String corpo) {
        System.out.println("\n📧 EMAIL ENVIADO");
        System.out.println("  Para: " + para);
        System.out.println("  Assunto: " + assunto);
        System.out.println("  Corpo:");
        System.out.println("  " + corpo.replace("\n", "\n  "));
    }

    private String gerarCorpoConfirmacao(Pedido pedido) {
        return "Olá " + pedido.getCliente().getNome() + ",\n\n" +
               "Seu pedido #" + pedido.getId() + " foi confirmado.\n" +
               "Data: " + formatarData(pedido.getDataCriacao()) + "\n" +
               "Total: R$ " + pedido.calcularTotal() + "\n\n" +
               "Aguardamos o pagamento.\n\n" +
               "Atenciosamente,\nEquipe E-commerce";
    }

    private String gerarCorpoPagamentoAprovado(Pedido pedido) {
        return "Olá " + pedido.getCliente().getNome() + ",\n\n" +
               "Seu pagamento foi APROVADO! ✓\n" +
               "Pedido: #" + pedido.getId() + "\n" +
               "Valor: R$ " + pedido.getPagamento().getValor() + "\n" +
               "Método: " + pedido.getPagamento().getMetodo() + "\n\n" +
               "Seu pedido será processado e enviado em breve.\n\n" +
               "Atenciosamente,\nEquipe E-commerce";
    }

    private String gerarCorpoFalhaPagamento(Pedido pedido) {
        return "Olá " + pedido.getCliente().getNome() + ",\n\n" +
               "Infelizmente sua transação foi recusada.\n" +
               "Pedido: #" + pedido.getId() + "\n" +
               "Valor: R$ " + pedido.getPagamento().getValor() + "\n\n" +
               "Por favor, tente novamente com outro método de pagamento.\n\n" +
               "Atenciosamente,\nEquipe E-commerce";
    }

    private String formatarData(LocalDateTime data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
