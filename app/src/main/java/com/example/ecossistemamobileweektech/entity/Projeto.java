package com.example.ecossistemamobileweektech.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidade que representa a tabela de projetos no banco de dados Room.
 * Armazena as informações dos projetos que os alunos desejam apresentar.
 */
@Entity(tableName = "projetos")
public class Projeto {
    
    // Identificador único do projeto com auto-incremento
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String nomeAluno;
    private String ra;
    private String nomeProjeto;
    private String descricao;
    private String data;
    private String horario;
    private boolean approved;
    private boolean hasCoffeeBreak;

    // Construtor para criar um novo objeto Projeto
    public Projeto(String nomeAluno, String ra, String nomeProjeto, String descricao, String data, String horario) {
        this.nomeAluno = nomeAluno;
        this.ra = ra;
        this.nomeProjeto = nomeProjeto;
        this.descricao = descricao;
        this.data = data;
        this.horario = horario;
        this.approved = false; // Por padrão, o projeto começa como não aprovado
        this.hasCoffeeBreak = false; // Por padrão, sem coffee break
    }

    // Métodos Getter e Setter para o Room acessar os dados
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNomeAluno() { return nomeAluno; }
    public void setNomeAluno(String nomeAluno) { this.nomeAluno = nomeAluno; }
    
    public String getRa() { return ra; }
    public void setRa(String ra) { this.ra = ra; }
    
    public String getNomeProjeto() { return nomeProjeto; }
    public void setNomeProjeto(String nomeProjeto) { this.nomeProjeto = nomeProjeto; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public boolean isHasCoffeeBreak() { return hasCoffeeBreak; }
    public void setHasCoffeeBreak(boolean hasCoffeeBreak) { this.hasCoffeeBreak = hasCoffeeBreak; }
}
