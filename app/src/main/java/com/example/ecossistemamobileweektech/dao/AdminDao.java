package com.example.ecossistemamobileweektech.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import com.example.ecossistemamobileweektech.entity.Admin;
import java.util.List;

@Dao
public interface AdminDao {
    @Insert
    void insert(Admin admin);

    @Update
    void update(Admin admin);

    @Delete
    void delete(Admin admin);

    @Query("SELECT * FROM admins WHERE matricula = :matricula AND password = :password LIMIT 1")
    Admin login(String matricula, String password);

    @Query("SELECT * FROM admins WHERE isSuperAdmin = 0")
    List<Admin> getAllExceptSuper();

    @Query("SELECT * FROM admins WHERE status = :status AND isSuperAdmin = 0")
    List<Admin> getByStatus(int status);

    @Query("SELECT * FROM admins WHERE matricula = :matricula LIMIT 1")
    Admin getByMatricula(String matricula);
}
