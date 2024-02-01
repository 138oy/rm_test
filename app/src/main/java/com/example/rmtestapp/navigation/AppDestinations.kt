package com.example.rmtestapp.navigation

interface AppDestination {
    val route: String
}

object MainScreen : AppDestination {
    override val route = "main_screen"
}
