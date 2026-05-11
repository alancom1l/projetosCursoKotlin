package com.example.crudapicep.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.crudapicep.R
import com.example.crudapicep.model.Contato
import com.example.crudapicep.viewmodel.ContatoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContatosScreen(
    viewModel: ContatoViewModel,
    onNavigateToCadastro: (Int?) -> Unit
) {
    val contatos by viewModel.contatos.collectAsStateWithLifecycle(initialValue = emptyList())
    var contatoParaDeletar by remember { mutableStateOf<Contato?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.title_contatos)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToCadastro(null) }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { padding ->
        if (contatos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.empty_list))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(contatos) { contato ->
                    ContatoItem(
                        contato = contato,
                        onClick = { onNavigateToCadastro(contato.id) },
                        onDelete = { contatoParaDeletar = contato }
                    )
                    HorizontalDivider()
                }
            }
        }

        // Dialog de Confirmação de Exclusão
        contatoParaDeletar?.let { contato ->
            AlertDialog(
                onDismissRequest = { contatoParaDeletar = null },
                title = { Text(stringResource(R.string.delete_confirm_title)) },
                text = { Text(stringResource(R.string.delete_confirm_msg)) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deletar(contato)
                        contatoParaDeletar = null
                    }) {
                        Text(stringResource(R.string.btn_excluir), color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { contatoParaDeletar = null }) {
                        Text(stringResource(R.string.btn_cancelar))
                    }
                }
            )
        }
    }
}

@Composable
fun ContatoItem(
    contato: Contato,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = { Text(contato.nome, fontWeight = FontWeight.Bold) },
        supportingContent = {
            Column {
                Text(contato.email)
                Text(contato.telefone)
            }
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Deletar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}