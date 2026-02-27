package com.kazedev.wher_sbro.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kazedev.wher_sbro.features.auth.presentation.screens.LoginScreen
import com.kazedev.wher_sbro.features.auth.presentation.screens.RegisterScreen
import com.kazedev.wher_sbro.features.radar.presentation.screens.LobbyScreen
import com.kazedev.wher_sbro.features.radar.presentation.screens.RadarScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onNavigateHome = {
                    navController.navigate(RadarLobbyRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RegisterRoute) { launchSingleTop = true }
                }
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(RadarLobbyRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable<RadarLobbyRoute> {
            LobbyScreen(
                onNavigateToRadar = { roomCode, targetName ->
                    navController.navigate(RadarRoute(roomCode, targetName)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<RadarRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<RadarRoute>()

            RadarScreen(
                roomCode = args.roomCode,
                targetName = args.targetName,
                onDisconnect = {
                    navController.navigate(RadarLobbyRoute) {
                        popUpTo(RadarLobbyRoute) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}