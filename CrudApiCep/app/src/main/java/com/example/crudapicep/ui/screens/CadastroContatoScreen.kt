package com.example.crudapicep.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.crudapicep.R
import com.example.crudapicep.model.Contato
import com.example.crudapicep.ui.components.InputField
import com.example.crudapicep.ui.components.PrimaryButton
import com.example.crudapicep.viewmodel.ContatoUiState
import com.example.crudapicep.viewmodel.ContatoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroContatoScreen(
    id: Int?,
    viewModel: ContatoViewModel,
    onBack: () -> Unit
) {
    val contatoSelecionado by viewModel.contatoSelecionado.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var nascimento by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var logradouro by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }

    // Carrega dados se for edição
    LaunchedEffect(id) {
        if (id != null) {
            viewModel.selecionarParaEdicao(id)
        } else {
            viewModel.limparSelecao()
        }
    }

    // Preenche campos quando o contato for carregado
    LaunchedEffect(contatoSelecionado) {
        contatoSelecionado?.let {
            nome = it.nome
            email = it.email
            telefone = it.telefone
            nascimento = it.nascimento
            cep = it.cep
            bairro = it.bairro
            logradouro = it.logradouro
            numero = it.numero
            estado = it.estado
            cidade = it.cidade
        }
    }

    // Reage ao estado da busca de CEP
    LaunchedEffect(uiState) {
        if (uiState is ContatoUiState.Success) {
            val endereco = (uiState as ContatoUiState.Success).endereco
            bairro = endereco.bairro
            logradouro = endereco.logradouro
            estado = endereco.estado
            cidade = endereco.cidade
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(stringResource(if (id == null) R.string.title_novo_contato else R.string.title_editar_contato)) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(label = stringResource(R.string.label_nome), value = nome, onValueChange = { nome = it })
            InputField(label = stringResource(R.string.label_email), value = email, onValueChange = { email = it }, keyboardType = KeyboardType.Email)
            InputField(label = stringResource(R.string.label_telefone), value = telefone, onValueChange = { telefone = it }, keyboardType = KeyboardType.Phone)
            InputField(label = stringResource(R.string.label_nascimento), value = nascimento, onValueChange = { nascimento = it })

            InputField(
                label = stringResource(R.string.label_cep),
                value = cep,
                onValueChange = { 
                    cep = it
                    if (it.length == 8) viewModel.buscarCep(it)
                },
                keyboardType = KeyboardType.Number,
                trailingIcon = {
                    IconButton(onClick = { viewModel.buscarCep(cep) }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar CEP")
                    }
                }
            )

            if (uiState is ContatoUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(8.dp))
            }

            if (uiState is ContatoUiState.Error) {
                Text((uiState as ContatoUiState.Error).message, color = MaterialTheme.colorScheme.error)
            }

            InputField(label = stringResource(R.string.label_logradouro), value = logradouro, onValueChange = { logradouro = it })
            InputField(label = stringResource(R.string.label_numero), value = numero, onValueChange = { numero = it }, keyboardType = KeyboardType.Number)
            InputField(label = stringResource(R.string.label_bairro), value = bairro, onValueChange = { bairro = it })
            InputField(label = stringResource(R.string.label_cidade), value = cidade, onValueChange = { cidade = it })
            InputField(label = stringResource(R.string.label_estado), value = estado, onValueChange = { estado = it })

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(R.string.btn_salvar),
                onClick = {
                    val contato = Contato(
                        id = id ?: 0,
                        nome = nome,
                        email = email,
                        telefone = telefone,
                        nascimento = nascimento,
                        cep = cep,
                        bairro = bairro,
                        logradouro = logradouro,
                        numero = numero,
                        estado = estado,
                        cidade = cidade
                    )
                    viewModel.salvar(contato)
                    onBack()
                }
            )
        }
    }
}