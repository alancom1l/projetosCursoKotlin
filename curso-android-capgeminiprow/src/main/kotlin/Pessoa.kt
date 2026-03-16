package org.example

/**
 * Classe modelo para representar uma pessoa
 * Demonstra o uso de Kotlin com data class
 */
data class Pessoa(
    var id: Int,
    var nome: String,
    var email: String,
    var idade: Int,
    var telefone: String? = null  // Null Safety: propriedade pode ser nula
) {
    /**
     * Valida se os dados da pessoa estão corretos
     */
    fun ehValida(): Boolean {
        return nome.isNotBlank() && 
               email.isNotBlank() && 
               idade in 1..120 &&
               email.contains("@")
    }

    /**
     * Retorna uma representação da pessoa
     */
    override fun toString(): String {
        val tel = telefone ?: "Não informado"  // Elvis Operator: retorna valor padrão se nulo
        return "ID: $id | Nome: $nome | Email: $email | Idade: $idade | Telefone: $tel"
    }
}

