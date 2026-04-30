package com.example.ecossistemamobileweektech;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidade que representa a tabela de participantes no banco de dados Room.
 * Cada instância desta classe será uma linha na tabela "participants".
 */
@Entity(tableName = "participants")
public class Participant {
    
    // Define a chave primária com auto-incremento
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;
    private String ra;
    private String course;
    private String series;
    private String activity;
    private boolean coffee; // Armazena se o participante vai ao Coffee Break
    private boolean attended; // Armazena se a presença foi confirmada pelo Admin

    // Construtor usado pelo Room e pela aplicação para criar um novo participante
    public Participant(String name, String ra, String course, String series, String activity, boolean coffee) {
        this.name = name;
        this.ra = ra;
        this.course = course;
        this.series = series;
        this.activity = activity;
        this.coffee = coffee;
        this.attended = false; // Por padrão, a presença começa como falsa
    }

    // Métodos Getter e Setter necessários para o Room acessar os campos privados
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
    
    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }
    
    public boolean isCoffee() { return coffee; }
    public void setCoffee(boolean coffee) { this.coffee = coffee; }
    
    public boolean isAttended() { return attended; }
    public void setAttended(boolean attended) { this.attended = attended; }
}
