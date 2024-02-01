package com.example.rmtestapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rmtestapp.navigation.MainScreen

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()):
        AppState {
    return remember(navController) { AppState(navController) }
}

class AppState(val navController: NavHostController) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    fun onGoBack() {
        if (navController.currentDestination != navController.graph.findNode(MainScreen.route)) {
            navController.popBackStack()
        }
    }
}