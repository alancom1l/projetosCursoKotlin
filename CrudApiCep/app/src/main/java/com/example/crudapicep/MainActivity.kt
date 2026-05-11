package com.example.crudapicep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.crudapicep.ui.screens.CadastroContatoScreen
import com.example.crudapicep.ui.screens.ListaContatosScreen
import com.example.crudapicep.ui.theme.CrudApiCepTheme
import com.example.crudapicep.viewmodel.ContatoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrudApiCepTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: ContatoViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaContatosScreen(
                viewModel = viewModel,
                onNavigateToCadastro = { id ->
                    if (id != null) {
                        navController.navigate("cadastro?id=$id")
                    } else {
                        navController.navigate("cadastro")
                    }
                }
            )
        }
        composable(
            route = "cadastro?id={id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id").takeIf { it != -1 }
            CadastroContatoScreen(
                id = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}