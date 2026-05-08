package com.example.ecossistemamobileweektech.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ecossistemamobileweektech.entity.Participante;

import java.util.List;

/**
 * Interface DAO (Data Access Object) para a entidade Participante.
 */
@Dao
public interface ParticipanteDao {
    
    @Insert
    void insert(Participante participant);

    @Update
    void update(Participante participant);

    @Delete
    void delete(Participante participant);

    @Query("SELECT * FROM participants")
    List<Participante> getAll();

    @Query("SELECT COUNT(*) FROM participants")
    int getCount();

    @Query("SELECT COUNT(*) FROM participants WHERE coffee = 1")
    int getCoffeeCount();

    @Query("SELECT * FROM participants WHERE activity = :activityName")
    List<Participante> getByActivity(String activityName);

    @Query("SELECT COUNT(*) FROM participants WHERE activity = :activityName")
    int getCountByActivity(String activityName);

    @Query("SELECT COUNT(*) FROM participants WHERE activity = :activityName AND coffee = 1")
    int getCoffeeCountByActivity(String activityName);

    @Query("SELECT * FROM participants WHERE activity = :activityName AND coffee = 1")
    List<Participante> getCoffeeParticipantsByActivity(String activityName);

    @Query("SELECT * FROM participants WHERE ra = :ra")
    List<Participante> getByRa(String ra);

    @Query("SELECT COUNT(*) FROM participants WHERE activity = :activityName AND attended = 1")
    int getAttendedCountByActivity(String activityName);

    @Query("SELECT * FROM participants WHERE activity = :activityName AND attended = 1")
    List<Participante> getAttendedParticipantsByActivity(String activityName);
}
