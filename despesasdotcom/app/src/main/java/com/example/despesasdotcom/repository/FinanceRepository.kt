package com.example.despesasdotcom.repository

import com.example.despesasdotcom.Earning
import com.example.despesasdotcom.Expense
import com.example.despesasdotcom.Dream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface FinanceRepository {
    fun getEarnings(): Flow<List<Earning>>
    fun getExpenses(): Flow<List<Expense>>
    fun getDreams(): Flow<List<Dream>>
    suspend fun addEarning(earning: Earning)
    suspend fun addExpense(expense: Expense)
    suspend fun addDream(dream: Dream)
}

@Singleton
class InMemoryFinanceRepository @Inject constructor() : FinanceRepository {
    private val _earnings = MutableStateFlow<List<Earning>>(emptyList())
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    private val _dreams = MutableStateFlow<List<Dream>>(emptyList())

    override fun getEarnings() = _earnings.asStateFlow()
    override fun getExpenses() = _expenses.asStateFlow()
    override fun getDreams() = _dreams.asStateFlow()

    override suspend fun addEarning(earning: Earning) {
        _earnings.value += earning
    }

    override suspend fun addExpense(expense: Expense) {
        _expenses.value += expense
    }

    override suspend fun addDream(dream: Dream) {
        _dreams.value += dream
    }
}
