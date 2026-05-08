package com.example.ecossistemamobileweektech.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.ecossistemamobileweektech.entity.Usuario;

@Dao
public interface UsuarioDao {
    @Insert
    void insert(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE ra = :ra AND password = :password LIMIT 1")
    Usuario login(String ra, String password);

    @Query("SELECT * FROM usuarios WHERE ra = :ra LIMIT 1")
    Usuario getByRA(String ra);
}
