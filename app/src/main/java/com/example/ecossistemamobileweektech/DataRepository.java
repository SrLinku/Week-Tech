package com.example.ecossistemamobileweektech;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositório de dados em memória para gerenciar administradores.
 * Utilizado para validação de acesso ao painel administrativo.
 */
public class DataRepository {
    private static DataRepository instance;
    private final List<Admin> admins;

    private DataRepository() {
        admins = new ArrayList<>();
        // Administradores padrão para testes e produção
        admins.add(new Admin("123", "123"));
        admins.add(new Admin("admin@unicesumar.edu.br", "cris123"));
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    /**
     * Adiciona um novo administrador à lista em memória.
     */
    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

    /**
     * Valida se as credenciais informadas pertencem a um administrador cadastrado.
     */
    public boolean validateAdmin(String email, String password) {
        for (Admin admin : admins) {
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
