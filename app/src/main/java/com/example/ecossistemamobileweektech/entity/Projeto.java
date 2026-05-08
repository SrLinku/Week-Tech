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
    private int status; // 0 = Pendente, 1 = Aprovado, 2 = Recusado, 3 = Solicitação de Cancelamento
    private String feedback; // Feedback do admin em caso de recusa ou motivo de cancelamento
    private boolean hasCoffeeBreak;

    @androidx.room.Ignore
    private String tipoEvento = "PROJETO";

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    @androidx.room.Ignore
    public Projeto() {}

    // Construtor para criar um novo objeto Projeto
    public Projeto(String nomeAluno, String ra, String nomeProjeto, String descricao, String data, String horario) {
        this.nomeAluno = nomeAluno;
        this.ra = ra;
        this.nomeProjeto = nomeProjeto;
        this.descricao = descricao;
        this.data = data;
        this.horario = horario;
        this.status = 0; // Inicia como Pendente
        this.hasCoffeeBreak = false;
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

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public boolean isApproved() { return status == 1; }
    public void setApproved(boolean approved) { this.status = approved ? 1 : 0; }

    public boolean isHasCoffeeBreak() { return hasCoffeeBreak; }
    public void setHasCoffeeBreak(boolean hasCoffeeBreak) { this.hasCoffeeBreak = hasCoffeeBreak; }
}
