package com.ecommerce.checkout.domain.entity;

import com.ecommerce.checkout.domain.exception.PedidoInvalidoException;

public class Cliente {
    
    private final String id;
    private final String nome;
    private final String email;
    private final String telefone;
    private final String cpf;

    public Cliente(String id, String nome, String email, String telefone, String cpf) {
        validar(id, nome, email, cpf);
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
    }

    private void validar(String id, String nome, String email, String cpf) {
        if (id == null || id.isBlank()) {
            throw new PedidoInvalidoException("ID do cliente não pode estar vazio");
        }
        if (nome == null || nome.isBlank()) {
            throw new PedidoInvalidoException("Nome do cliente não pode estar vazio");
        }
        if (email == null || !email.contains("@")) {
            throw new PedidoInvalidoException("Email do cliente inválido");
        }
        if (cpf == null || cpf.length() != 11) {
            throw new PedidoInvalidoException("CPF do cliente deve conter 11 dígitos");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
