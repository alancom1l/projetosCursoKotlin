package com.example.crudapicep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crudapicep.model.Contato
import com.example.crudapicep.model.EnderecoViaCep
import com.example.crudapicep.repository.ContatoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ContatoUiState {
    object Idle : ContatoUiState()
    object Loading : ContatoUiState()
    data class Success(val endereco: EnderecoViaCep) : ContatoUiState()
    data class Error(val message: String) : ContatoUiState()
}

@HiltViewModel
class ContatoViewModel @Inject constructor(
    private val repository: ContatoRepository
) : ViewModel() {

    val contatos = repository.allContatos

    private val _uiState = MutableStateFlow<ContatoUiState>(ContatoUiState.Idle)
    val uiState: StateFlow<ContatoUiState> = _uiState.asStateFlow()

    private val _contatoSelecionado = MutableStateFlow<Contato?>(null)
    val contatoSelecionado: StateFlow<Contato?> = _contatoSelecionado.asStateFlow()

    fun buscarCep(cep: String) {
        if (cep.length != 8) return

        viewModelScope.launch {
            _uiState.value = ContatoUiState.Loading
            repository.buscarEnderecoPorCep(cep)
                .onSuccess { _uiState.value = ContatoUiState.Success(it) }
                .onFailure { _uiState.value = ContatoUiState.Error("CEP não encontrado") }
        }
    }

    fun selecionarParaEdicao(id: Int) {
        viewModelScope.launch {
            _contatoSelecionado.value = repository.buscarPorId(id)
        }
    }

    fun limparSelecao() {
        _contatoSelecionado.value = null
        _uiState.value = ContatoUiState.Idle
    }

    fun salvar(contato: Contato) {
        viewModelScope.launch {
            if (contato.id == 0) {
                repository.inserir(contato)
            } else {
                repository.atualizar(contato)
            }
        }
    }

    fun deletar(contato: Contato) {
        viewModelScope.launch {
            repository.deletar(contato)
        }
    }

    // Validações básicas
    fun validarContato(contato: Contato): String? {
        return when {
            contato.nome.isBlank() -> "Nome é obrigatório"
            !contato.email.contains("@") -> "E-mail inválido"
            contato.telefone.length < 8 -> "Telefone inválido"
            contato.cep.length != 8 -> "CEP deve ter 8 dígitos"
            else -> null
        }
    }
}