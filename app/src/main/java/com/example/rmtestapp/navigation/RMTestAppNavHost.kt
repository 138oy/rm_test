package com.example.rmtestapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rmtestapp.AppState
import com.example.rmtestapp.screens.MyHomeScreen

@Composable
fun RMTestAppNavHost(
    navController: NavHostController,
    startDestination: String,
    appState: AppState
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = MainScreen.route) {
            MyHomeScreen(navController)
        }

    }
}
