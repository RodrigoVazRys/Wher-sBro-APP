package com.kazedev.wher_sbro.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kazedev.wher_sbro.features.auth.presentation.screens.LoginScreen
import com.kazedev.wher_sbro.features.auth.presentation.screens.RegisterScreen
import com.kazedev.wher_sbro.features.auth.presentation.viewmodels.LoginViewModel
import com.kazedev.wher_sbro.features.auth.presentation.viewmodels.RegisterViewModel
import com.kazedev.wher_sbro.features.radar.presentation.screens.LobbyScreen
import com.kazedev.wher_sbro.features.radar.presentation.screens.RadarScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RadarRoute // Arranca directo en el Radar
    ) {

        // --- 1. PANTALLA DE LOGIN ---
        composable<LoginRoute> {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigateHome = {
                    navController.navigate(RadarLobbyRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RegisterRoute)
                }
            )
        }

        // --- 2. PANTALLA DE REGISTRO ---
        composable<RegisterRoute> {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate(RadarLobbyRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // --- 3. PANTALLA PRINCIPAL (LOBBY) ---
//        composable<RadarLobbyRoute> {
//            // Pasamos los callbacks reales para que funcione cuando la uses
//            LobbyScreen(
//                onNavigateToRadar = { room, user ->
//                    navController.navigate(RadarRoute)
//                }
//            )
//        }

        // --- 4. PANTALLA DEL RADAR ---
        composable<RadarRoute> {
            RadarScreen(
                roomCode = "2R2DA",     // Valor hardcodeado temporal
                targetName = "ROY",  // Valor hardcodeado temporal
                onDisconnect = {
                    // Para probar, hacemos que el botón de desconexión nos regrese al Lobby
                    navController.navigate(RadarLobbyRoute) {
                        popUpTo(RadarRoute) { inclusive = true }
                    }
                }
            )
        }
    }
}