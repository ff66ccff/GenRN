package com.randomnumbergenerator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.randomnumbergenerator.screens.AboutScreen
import com.randomnumbergenerator.screens.RandomScreen

sealed class NavigationItem(val route: String) {
    object Home : NavigationItem("home")
    object About : NavigationItem("about")
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            RandomScreen(
                onNavigateToAbout = { navController.navigate(NavigationItem.About.route) }
            )
        }
        composable(NavigationItem.About.route) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
