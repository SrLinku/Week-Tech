package com.example.ecossistemamobileweektech.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ecossistemamobileweektech.Participant;

import java.util.List;

/**
 * Interface DAO (Data Access Object) para a entidade Participant.
 * Define as operações de banco de dados (SQL) que podem ser realizadas.
 */
@Dao
public interface ParticipanteDao {
    
    // Insere um novo participante no banco de dados
    @Insert
    void insert(Participant participant);

    // Atualiza os dados de um participante existente (ex: marcar presença)
    @Update
    void update(Participant participant);

    // Remove um participante do banco de dados
    @Delete
    void delete(Participant participant);

    // Retorna a lista de todos os participantes cadastrados
    @Query("SELECT * FROM participants")
    List<Participant> getAll();

    // Retorna a quantidade total de participantes inscritos
    @Query("SELECT COUNT(*) FROM participants")
    int getCount();

    // Retorna a quantidade de participantes que confirmaram Coffee Break
    @Query("SELECT COUNT(*) FROM participants WHERE coffee = 1")
    int getCoffeeCount();

    // Retorna a lista de participantes inscritos em uma atividade específica
    @Query("SELECT * FROM participants WHERE activity = :activityName")
    List<Participant> getByActivity(String activityName);

    // Retorna a quantidade de participantes em uma atividade específica
    @Query("SELECT COUNT(*) FROM participants WHERE activity = :activityName")
    int getCountByActivity(String activityName);

    // Retorna a quantidade de coffee break em uma atividade específica
    @Query("SELECT COUNT(*) FROM participants WHERE activity = :activityName AND coffee = 1")
    int getCoffeeCountByActivity(String activityName);

    // Retorna a lista de nomes para coffee break em uma atividade específica
    @Query("SELECT * FROM participants WHERE activity = :activityName AND coffee = 1")
    List<Participant> getCoffeeParticipantsByActivity(String activityName);
}
