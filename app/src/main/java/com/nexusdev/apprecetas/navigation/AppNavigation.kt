package com.nexusdev.apprecetas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexusdev.apprecetas.presentation.ui.AddRecetas
import com.nexusdev.apprecetas.presentation.ui.DetalleRecetaScreen
import com.nexusdev.apprecetas.presentation.ui.LoginScreen
import com.nexusdev.apprecetas.presentation.ui.RecetasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("recetas") {
            RecetasScreen(navController)
        }
        composable("detalle/{recetaId}") { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("recetaId") ?: return@composable
            DetalleRecetaScreen(navController, recetaId)
        }
        composable("add") {
            AddRecetas(navController)
        }
    }
}