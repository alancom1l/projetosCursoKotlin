package com.example.crudapicep.model

import com.google.gson.annotations.SerializedName

data class EnderecoViaCep(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    @SerializedName("localidade")
    val cidade: String,
    @SerializedName("uf")
    val estado: String
)