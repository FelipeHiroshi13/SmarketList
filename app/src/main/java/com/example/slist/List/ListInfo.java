package com.example.slist.List;

public class ListInfo {
    private Long id = -1L;
    private String nome;
    private String quantidade;
    private String categoria;
    private int peso;
    private String ok;
    private String sugestion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getSugestion() {
        return sugestion;
    }

    public void setSugestion(String sugestion) {
        this.sugestion = sugestion;
    }
}
