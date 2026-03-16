package org.example

/**
 * Classe gerenciadora de pessoas
 * Implementa padrão de repositório para CRUD operations
 */
class AgendaPessoas {
    private val pessoas = mutableListOf<Pessoa>()
    private var proximoId = 1

    /**
     * Cadastra uma nova pessoa na agenda
     */
    fun cadastrar(nome: String, email: String, idade: Int, telefone: String? = null): Boolean {
        // Validações
        if (nome.isBlank()) {
            println("❌ Erro: Nome não pode estar vazio!")
            return false
        }
        
        if (email.isBlank() || !email.contains("@")) {
            println("❌ Erro: Email inválido! Deve conter '@'")
            return false
        }
        
        if (idade < 1 || idade > 120) {
            println("❌ Erro: Idade deve estar entre 1 e 120 anos!")
            return false
        }
        
        // Verifica se email já existe
        if (pessoas.any { it.email.equals(email, ignoreCase = true) }) {
            println("❌ Erro: Email já cadastrado!")
            return false
        }
        
        val pessoa = Pessoa(proximoId++, nome, email, idade, telefone)
        
        if (pessoa.ehValida()) {
            pessoas.add(pessoa)
            println("✅ Pessoa cadastrada com sucesso! ID: ${pessoa.id}")
            return true
        } else {
            println("❌ Erro ao validar dados da pessoa!")
            return false
        }
    }

    /**
     * Lista todas as pessoas cadastradas
     */
    fun listar(): Boolean {
        return if (pessoas.isEmpty()) {
            println("❌ Nenhuma pessoa cadastrada!")
            false
        } else {
            println("\n========== LISTA DE PESSOAS ==========")
            pessoas.forEach { println(it) }
            println("=====================================\n")
            true
        }
    }

    /**
     * Pesquisa pessoas por nome (parcial)
     */
    fun pesquisar(nome: String): Boolean {
        val resultados = pessoas.filter { it.nome.contains(nome, ignoreCase = true) }
        
        return if (resultados.isEmpty()) {
            println("❌ Nenhuma pessoa encontrada com o nome: $nome")
            false
        } else {
            println("\n========== RESULTADOS DA BUSCA ==========")
            resultados.forEach { println(it) }
            println("========================================\n")
            true
        }
    }

    /**
     * Altera os dados de uma pessoa pelo ID
     * Demonstra uso de Null Safety com safe call operator
     */
    fun alterar(id: Int, novoNome: String?, novoEmail: String?, novaIdade: Int?): Boolean {
        val pessoa = pessoas.find { it.id == id }
        
        // Null Safety: utiliza safe call e Elvis operator
        val pessoaEncontrada = pessoa?.let { p ->
            // Atualiza apenas os campos fornecidos
            if (novoNome != null && novoNome.isNotBlank()) {
                p.nome = novoNome
            }
            
            if (novoEmail != null && novoEmail.isNotBlank()) {
                if (!novoEmail.contains("@")) {
                    println("❌ Email inválido!")
                    return@let null
                }
                
                if (pessoas.any { it.id != id && it.email.equals(novoEmail, ignoreCase = true) }) {
                    println("❌ Email já está em uso!")
                    return@let null
                }
                
                p.email = novoEmail
            }
            
            if (novaIdade != null && novaIdade in 1..120) {
                p.idade = novaIdade
            }
            
            p
        } ?: return false
        
        if (pessoaEncontrada.ehValida()) {
            println("✅ Pessoa atualizada com sucesso!")
            return true
        } else {
            println("❌ Dados inválidos após atualização!")
            return false
        }
    }

    /**
     * Remove uma pessoa pelo ID
     */
    fun remover(id: Int): Boolean {
        val pessoa = pessoas.find { it.id == id }
        
        return if (pessoa != null) {
            pessoas.remove(pessoa)
            println("✅ Pessoa removida com sucesso!")
            true
        } else {
            println("❌ Pessoa com ID $id não encontrada!")
            false
        }
    }

    /**
     * Obtém uma pessoa pelo ID (Null Safety)
     */
    fun obterPorId(id: Int): Pessoa? = pessoas.find { it.id == id }

    /**
     * Retorna o total de pessoas cadastradas
     */
    fun obterTotal(): Int = pessoas.size
}

