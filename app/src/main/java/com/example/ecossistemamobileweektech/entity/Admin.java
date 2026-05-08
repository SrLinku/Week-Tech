package com.example.ecossistemamobileweektech.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidade que representa um Administrador no sistema.
 */
@Entity(tableName = "admins")
public class Admin {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String matricula; // E-mail ou matrícula
    private String password;
    private String name;
    private int status; // 0 = Pendente, 1 = Aprovado
    private boolean isSuperAdmin;

    public Admin(String name, String matricula, String password, boolean isSuperAdmin) {
        this.name = name;
        this.matricula = matricula;
        this.password = password;
        this.isSuperAdmin = isSuperAdmin;
        this.status = isSuperAdmin ? 1 : 0; // Super admin já nasce aprovado
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public boolean isSuperAdmin() { return isSuperAdmin; }
    public void setSuperAdmin(boolean superAdmin) { isSuperAdmin = superAdmin; }
}
