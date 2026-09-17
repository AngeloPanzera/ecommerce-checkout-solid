package com.ecommerce.checkout.domain.entity;

import com.ecommerce.checkout.domain.exception.PedidoInvalidoException;
import java.math.BigDecimal;

public class ItemPedido {
    
    private final String skuProduto;
    private final String nomeProduto;
    private final int quantidade;
    private final BigDecimal precoUnitario;

    public ItemPedido(String skuProduto, String nomeProduto, int quantidade, BigDecimal precoUnitario) {
        validar(skuProduto, nomeProduto, quantidade, precoUnitario);
        this.skuProduto = skuProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    private void validar(String sku, String nome, int qtd, BigDecimal preco) {
        if (sku == null || sku.isBlank()) {
            throw new PedidoInvalidoException("SKU do produto não pode estar vazio");
        }
        if (nome == null || nome.isBlank()) {
            throw new PedidoInvalidoException("Nome do produto não pode estar vazio");
        }
        if (qtd <= 0) {
            throw new PedidoInvalidoException("Quantidade deve ser maior que zero");
        }
        if (preco == null || preco.signum() <= 0) {
            throw new PedidoInvalidoException("Preço deve ser maior que zero");
        }
    }

    public BigDecimal calcularTotal() {
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }

    // Getters
    public String getSkuProduto() {
        return skuProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "sku='" + skuProduto + '\'' +
                ", nome='" + nomeProduto + '\'' +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", total=" + calcularTotal() +
                '}';
    }
}
