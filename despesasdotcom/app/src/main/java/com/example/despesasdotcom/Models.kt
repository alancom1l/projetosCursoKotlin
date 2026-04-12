package com.example.despesasdotcom

import java.util.UUID

data class Earning(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val value: Double,
    val month: String
)

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val value: Double,
    val month: String
)

data class Dream(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val value: Double
)
