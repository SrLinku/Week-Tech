package com.example.ecossistemamobileweektech.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidade que representa um Aluno/Usuário no sistema.
 */
@Entity(tableName = "usuarios")
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;
    private String ra;
    private String course;
    private String series;
    private String password;

    public Usuario(String name, String ra, String course, String series, String password) {
        this.name = name;
        this.ra = ra;
        this.course = course;
        this.series = series;
        this.password = password;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRa() { return ra; }
    public void setRa(String ra) { this.ra = ra; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getSeries() { return series; }
    public void setSeries(String series) { this.series = series; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
