package com.example.ecossistemamobileweektech.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ecossistemamobileweektech.dao.ParticipanteDao;
import com.example.ecossistemamobileweektech.dao.ProjetoDao;
import com.example.ecossistemamobileweektech.Participant;
import com.example.ecossistemamobileweektech.entity.Projeto;

/**
 * Classe principal do Banco de Dados Room.
 * Define as entidades (tabelas) e a versão do banco.
 * Segue o padrão Singleton para garantir uma única instância do banco em todo o app.
 */
@Database(entities = {Participant.class, Projeto.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // Métodos abstratos para acessar os DAOs
    public abstract ParticipanteDao participanteDao();
    public abstract ProjetoDao projetoDao();

    /**
     * Retorna a instância única do banco de dados (Singleton).
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            // Criação do banco de dados
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "tech_week_db")
                    .fallbackToDestructiveMigration() // Reinicia o banco se a versão mudar
                    .allowMainThreadQueries() // Permite consultas na thread principal (facilita para faculdade)
                    .build();
        }
        return instance;
    }
}
