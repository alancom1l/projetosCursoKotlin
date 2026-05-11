package com.example.crudapicep.data.local

import androidx.room.*
import com.example.crudapicep.model.Contato
import kotlinx.coroutines.flow.Flow

@Dao
interface ContatoDao {
    @Query("SELECT * FROM contatos ORDER BY nome ASC")
    fun getAll(): Flow<List<Contato>>

    @Query("SELECT * FROM contatos WHERE id = :id")
    suspend fun getById(id: Int): Contato?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contato: Contato)

    @Update
    suspend fun update(contato: Contato)

    @Delete
    suspend fun delete(contato: Contato)
}