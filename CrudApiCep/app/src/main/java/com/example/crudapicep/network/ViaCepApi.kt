package com.example.crudapicep.network

import com.example.crudapicep.model.EnderecoViaCep
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepApi {
    @GET("ws/{cep}/json/")
    suspend fun buscarEndereco(@Path("cep") cep: String): Response<EnderecoViaCep>
}