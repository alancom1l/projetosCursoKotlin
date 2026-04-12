package com.example.despesasdotcom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Extensão utilitária para formatação de moeda
fun Double.toCurrency(): String = "R$ %.2f".format(this)

@Composable
fun FinanceApp(viewModel: FinanceViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    NavigationItem("home", Icons.Default.Home, stringResource(R.string.home_title)),
                    NavigationItem("earnings", Icons.Default.AttachMoney, stringResource(R.string.earnings_title)),
                    NavigationItem("expenses", Icons.Default.MoneyOff, stringResource(R.string.expenses_title)),
                    NavigationItem("dreams", Icons.Default.Star, stringResource(R.string.dreams_title))
                )

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = { 
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(viewModel) }
            composable("earnings") { 
                FinanceEntryScreen(
                    title = stringResource(R.string.earnings_title),
                    icon = Icons.Default.AttachMoney,
                    iconColor = Color(0xFF4CAF50),
                    items = viewModel.earnings.collectAsState().value.map { it.toCommon() },
                    onAdd = { desc, value, month -> viewModel.addEarning(desc, value, month) },
                    buttonText = stringResource(R.string.add_earning)
                )
            }
            composable("expenses") { 
                FinanceEntryScreen(
                    title = stringResource(R.string.expenses_title),
                    icon = Icons.Default.MoneyOff,
                    iconColor = Color.Red,
                    items = viewModel.expenses.collectAsState().value.map { it.toCommon() },
                    onAdd = { desc, value, month -> viewModel.addExpense(desc, value, month) },
                    buttonText = stringResource(R.string.add_expense)
                )
            }
            composable("dreams") { DreamsScreen(viewModel) }
        }
    }
}

data class NavigationItem(val route: String, val icon: ImageVector, val label: String)

// Modelos comuns para reutilização de UI
data class CommonEntry(val description: String, val value: Double, val month: String)
fun Earning.toCommon() = CommonEntry(description, value, month)
fun Expense.toCommon() = CommonEntry(description, value, month)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceEntryScreen(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    items: List<CommonEntry>,
    onAdd: (String, Double, String) -> Unit,
    buttonText: String
) {
    var description by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }

    val isInputValid = description.isNotBlank() && (value.toDoubleOrNull() ?: 0.0) > 0.0 && month.isNotBlank()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = iconColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = description, 
            onValueChange = { description = it }, 
            label = { Text(stringResource(R.string.description_label)) }, 
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text(stringResource(R.string.value_label)) },
            modifier = Modifier.fillMaxWidth(),
            prefix = { Text(stringResource(R.string.currency_prefix)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(8.dp))
        MonthDropdown(selectedMonth = month, onMonthSelected = { month = it })
        
        Button(
            onClick = {
                val v = value.replace(",", ".").toDoubleOrNull() ?: 0.0
                onAdd(description, v, month)
                description = ""; value = ""; month = ""
            },
            enabled = isInputValid,
            modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
        ) {
            Text(buttonText)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        LazyColumn {
            items(items) { item ->
                ListItem(
                    headlineContent = { Text(item.description) },
                    supportingContent = { Text(item.month) },
                    trailingContent = { Text(item.value.toCurrency(), color = iconColor) },
                    leadingContent = { Icon(icon, contentDescription = null, tint = iconColor) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthDropdown(selectedMonth: String, onMonthSelected: (String) -> Unit) {
    val months = listOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedMonth,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.month_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            placeholder = { Text(stringResource(R.string.select_month)) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            months.forEach { month ->
                DropdownMenuItem(
                    text = { Text(month) },
                    onClick = {
                        onMonthSelected(month)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: FinanceViewModel) {
    val balance by viewModel.balance.collectAsState(initial = 0.0)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.account_balance), fontSize = 20.sp)
        Text(
            text = balance.toCurrency(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = if (balance >= 0) Color(0xFF4CAF50) else Color.Red
        )
    }
}

@Composable
fun DreamsScreen(viewModel: FinanceViewModel) {
    val dreams by viewModel.dreams.collectAsState()
    val balance by viewModel.balance.collectAsState(initial = 0.0)
    var description by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    val isInputValid = description.isNotBlank() && (value.toDoubleOrNull() ?: 0.0) > 0.0

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.dreams_title), style = MaterialTheme.typography.headlineMedium)
        }
        Text(stringResource(R.string.current_balance, balance.toCurrency()), color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = description, 
            onValueChange = { description = it }, 
            label = { Text(stringResource(R.string.dream_input_hint)) }, 
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text(stringResource(R.string.dream_value_hint)) },
            modifier = Modifier.fillMaxWidth(),
            prefix = { Text(stringResource(R.string.currency_prefix)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        
        Button(
            onClick = {
                val v = value.replace(",", ".").toDoubleOrNull() ?: 0.0
                viewModel.addDream(description, v)
                description = ""; value = ""
            },
            enabled = isInputValid,
            modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.add_dream))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        LazyColumn {
            items(dreams) { dream ->
                val progress = if (dream.value > 0) (balance / dream.value).coerceIn(0.0, 1.0).toFloat() else 0f
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(dream.description, fontWeight = FontWeight.Bold)
                        }
                        Text(stringResource(R.string.dream_meta, dream.value.toCurrency()))
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                        Text(stringResource(R.string.dream_progress, (progress * 100).toInt()), fontSize = 12.sp, modifier = Modifier.align(Alignment.End))
                    }
                }
            }
        }
    }
}
