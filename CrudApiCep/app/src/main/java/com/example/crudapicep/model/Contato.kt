package com.example.crudapicep.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contatos")
data class Contato(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val email: String,
    val telefone: String,
    val nascimento: String,
    val cep: String,
    val bairro: String,
    val logradouro: String,
    val numero: String,
    val estado: String,
    val cidade: String
)