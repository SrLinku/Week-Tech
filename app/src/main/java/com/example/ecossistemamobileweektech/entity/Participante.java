package com.example.ecossistemamobileweektech.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "participantes")
public class Participante {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String nome;
    private String ra;
    private String curso;
    private String serie;
    private boolean coffeeBreak;

    public Participante(String nome, String ra, String curso, String serie, boolean coffeeBreak) {
        this.nome = nome;
        this.ra = ra;
        this.curso = curso;
        this.serie = serie;
        this.coffeeBreak = coffeeBreak;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getRa() { return ra; }
    public void setRa(String ra) { this.ra = ra; }
    
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    
    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }
    
    public boolean isCoffeeBreak() { return coffeeBreak; }
    public void setCoffeeBreak(boolean coffeeBreak) { this.coffeeBreak = coffeeBreak; }
}
