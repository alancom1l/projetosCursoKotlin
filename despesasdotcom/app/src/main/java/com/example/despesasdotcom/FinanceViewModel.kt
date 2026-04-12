package com.example.despesasdotcom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.despesasdotcom.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val repository: FinanceRepository
) : ViewModel() {

    val earnings: StateFlow<List<Earning>> = repository.getEarnings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<Expense>> = repository.getExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dreams: StateFlow<List<Dream>> = repository.getDreams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val balance = combine(earnings, expenses) { earnings, expenses ->
        earnings.sumOf { it.value } - expenses.sumOf { it.value }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addEarning(description: String, value: Double, month: String) {
        viewModelScope.launch {
            repository.addEarning(Earning(description = description, value = value, month = month))
        }
    }

    fun addExpense(description: String, value: Double, month: String) {
        viewModelScope.launch {
            repository.addExpense(Expense(description = description, value = value, month = month))
        }
    }

    fun addDream(description: String, value: Double) {
        viewModelScope.launch {
            repository.addDream(Dream(description = description, value = value))
        }
    }
}
