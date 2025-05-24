package com.br.venceem;

// Classe modelo que representa um Produto
public class Produto {
    private int id;
    private String nome;
    private String validade; // esperado no formato yyyy-MM-dd para armazenamento

    public Produto() {
    }

    public Produto(int id, String nome, String validade) {
        this.id = id;
        this.nome = nome;
        this.validade = validade;
    }

    public int obterId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String obterNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String obterValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }
}
