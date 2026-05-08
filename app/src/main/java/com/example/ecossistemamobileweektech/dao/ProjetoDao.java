package com.example.ecossistemamobileweektech.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ecossistemamobileweektech.entity.Projeto;

import java.util.List;

/**
 * Interface DAO (Data Access Object) para a entidade Projeto.
 * Define como o aplicativo interage com a tabela de projetos.
 */
@Dao
public interface ProjetoDao {
    
    // Insere um novo projeto cadastrado pelo aluno no banco de dados
    @Insert
    void insert(Projeto projeto);

    // Atualiza os dados de um projeto já existente
    @Update
    void update(Projeto projeto);

    // Remove um projeto do banco de dados
    @Delete
    void delete(Projeto projeto);

    // Retorna a lista de todos os projetos cadastrados no evento
    @Query("SELECT * FROM projetos")
    List<Projeto> getAll();

    // Retorna apenas os projetos que já foram aprovados pelo admin (status = 1)
    @Query("SELECT * FROM projetos WHERE status = 1")
    List<Projeto> getApproved();

    // Retorna projetos por status específico (0=Pendente, 1=Aprovado, 2=Recusado)
    @Query("SELECT * FROM projetos WHERE status = :status")
    List<Projeto> getByStatus(int status);

    // Busca um projeto específico pelo nome
    @Query("SELECT * FROM projetos WHERE nomeProjeto = :projectName LIMIT 1")
    Projeto getByName(String projectName);

    // Busca projetos específicos através do RA do aluno
    @Query("SELECT * FROM projetos WHERE ra = :ra")
    List<Projeto> getByRa(String ra);
}
