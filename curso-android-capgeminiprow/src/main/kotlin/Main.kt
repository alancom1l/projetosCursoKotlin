package org.example

import java.util.Scanner

fun main() {
    val agenda = AgendaPessoas()
    val scanner = Scanner(System.`in`)
    var opcao = 0

    println("╔════════════════════════════════════════╗")
    println("║    AGENDA DE PESSOAS - KOTLIN          ║")
    println("║    Sistema de Gerenciamento CRUD       ║")
    println("╚════════════════════════════════════════╝\n")

    // Laço de repetição do menu principal
    do {
        exibirMenu()
        print("Digite sua opção: ")
        
        opcao = try {
            scanner.nextInt()
        } catch (e: Exception) {
            scanner.nextLine()  // Limpa o buffer
            println("❌ Entrada inválida! Digite um número.")
            0
        }

        scanner.nextLine()  // Limpa o buffer após nextInt()

        when (opcao) {
            1 -> executarCadastro(agenda, scanner)
            2 -> agenda.listar()
            3 -> executarPesquisa(agenda, scanner)
            4 -> executarAlteracao(agenda, scanner)
            5 -> executarRemocao(agenda, scanner)
            6 -> {
                println("\n╔════════════════════════════════════════╗")
                println("║  Obrigado por usar o programa!         ║")
                println("║  Total de pessoas: ${agenda.obterTotal()}")
                println("╚════════════════════════════════════════╝")
            }
            else -> println("❌ Opção inválida! Tente novamente.\n")
        }

    } while (opcao != 6)

    scanner.close()
}

/**
 * Exibe o menu de opções
 */
fun exibirMenu() {
    println("\n┌────── MENU PRINCIPAL ──────┐")
    println("│ 1 - Cadastrar Pessoa       │")
    println("│ 2 - Listar Pessoas         │")
    println("│ 3 - Pesquisar Pessoa       │")
    println("│ 4 - Alterar Pessoa         │")
    println("│ 5 - Remover Pessoa         │")
    println("│ 6 - Finalizar              │")
    println("└────────────────────────────┘")
}

/**
 * Executa a operação de cadastro
 */
fun executarCadastro(agenda: AgendaPessoas, scanner: Scanner) {
    println("\n========== CADASTRO DE PESSOA ==========")
    
    print("Nome: ")
    val nome = scanner.nextLine()
    
    print("Email: ")
    val email = scanner.nextLine()
    
    print("Idade: ")
    val idade = try {
        scanner.nextLine().toInt()
    } catch (e: NumberFormatException) {
        println("❌ Idade inválida!")
        return
    }
    
    print("Telefone (pressione Enter para pular): ")
    val telefone = scanner.nextLine().takeIf { it.isNotBlank() }
    
    agenda.cadastrar(nome, email, idade, telefone)
    println("========================================\n")
}

/**
 * Executa a operação de pesquisa
 */
fun executarPesquisa(agenda: AgendaPessoas, scanner: Scanner) {
    println("\n========== PESQUISAR PESSOA ==========")
    print("Digite o nome (ou parte dele): ")
    val nome = scanner.nextLine()
    
    agenda.pesquisar(nome)
}

/**
 * Executa a operação de alteração
 */
fun executarAlteracao(agenda: AgendaPessoas, scanner: Scanner) {
    println("\n========== ALTERAR PESSOA ==========")
    
    agenda.listar()
    
    print("Digite o ID da pessoa a alterar: ")
    val id = try {
        scanner.nextLine().toInt()
    } catch (e: NumberFormatException) {
        println("❌ ID inválido!")
        return
    }
    
    val pessoa = agenda.obterPorId(id)
    if (pessoa == null) {
        println("❌ Pessoa com ID $id não encontrada!")
        return
    }
    
    println("\nPessoa encontrada: $pessoa")
    println("\nPara não alterar um campo, deixe em branco\n")
    
    print("Novo nome (${pessoa.nome}): ")
    val novoNome = scanner.nextLine().takeIf { it.isNotBlank() }
    
    print("Novo email (${pessoa.email}): ")
    val novoEmail = scanner.nextLine().takeIf { it.isNotBlank() }
    
    print("Nova idade (${pessoa.idade}): ")
    val novaIdade = try {
        scanner.nextLine().takeIf { it.isNotBlank() }?.toInt()
    } catch (e: NumberFormatException) {
        println("❌ Idade inválida!")
        return
    }
    
    agenda.alterar(id, novoNome, novoEmail, novaIdade)
    println("=====================================\n")
}

/**
 * Executa a operação de remoção
 */
fun executarRemocao(agenda: AgendaPessoas, scanner: Scanner) {
    println("\n========== REMOVER PESSOA ==========")
    
    agenda.listar()
    
    print("Digite o ID da pessoa a remover: ")
    val id = try {
        scanner.nextLine().toInt()
    } catch (e: NumberFormatException) {
        println("❌ ID inválido!")
        return
    }
    
    print("Tem certeza? (s/n): ")
    val confirmacao = scanner.nextLine()
    
    if (confirmacao.equals("s", ignoreCase = true)) {
        agenda.remover(id)
    } else {
        println("❌ Operação cancelada!")
    }
    
    println("====================================\n")
}