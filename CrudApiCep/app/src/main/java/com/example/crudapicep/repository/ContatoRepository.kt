package com.example.crudapicep.repository

import com.example.crudapicep.data.local.ContatoDao
import com.example.crudapicep.model.Contato
import com.example.crudapicep.model.EnderecoViaCep
import com.example.crudapicep.network.ViaCepApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContatoRepository @Inject constructor(
    private val contatoDao: ContatoDao,
    private val viaCepApi: ViaCepApi
) {
    val allContatos: Flow<List<Contato>> = contatoDao.getAll()

    suspend fun buscarPorId(id: Int): Contato? {
        return contatoDao.getById(id)
    }

    suspend fun inserir(contato: Contato) {
        contatoDao.insert(contato)
    }

    suspend fun atualizar(contato: Contato) {
        contatoDao.update(contato)
    }

    suspend fun deletar(contato: Contato) {
        contatoDao.delete(contato)
    }

    suspend fun buscarEnderecoPorCep(cep: String): Result<EnderecoViaCep> {
        return try {
            val response = viaCepApi.buscarEndereco(cep)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro ao buscar CEP"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}