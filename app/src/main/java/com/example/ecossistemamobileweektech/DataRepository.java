package com.example.ecossistemamobileweektech;

import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private static DataRepository instance;
    private List<Participant> participants;
    private List<Admin> admins;

    private DataRepository() {
        participants = new ArrayList<>();
        admins = new ArrayList<>();
        // Adicionando administrador padrão
        admins.add(new Admin("123", "123"));
        admins.add(new Admin("admin@unicesumar.edu.br", "cris123"));
        
        // Adicionando alguns dados mockados para o Admin ver inicialmente
        participants.add(new Participant("João Silva", "123456", "ADS", "1ª", "Workshop: Flutter", true));
        participants.add(new Participant("Maria Oliveira", "654321", "Software", "2ª", "UI/UX Design", false));
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

    public boolean validateAdmin(String email, String password) {
        for (Admin admin : admins) {
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}