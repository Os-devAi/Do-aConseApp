package com.nexusdev.apprecetas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexusdev.apprecetas.presentation.ui.RecetasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "recetas") {
        composable("recetas") {
            RecetasScreen(navController)
        }
    }
}