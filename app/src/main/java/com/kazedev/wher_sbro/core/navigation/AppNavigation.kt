package com.kazedev.wher_sbro.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kazedev.wher_sbro.features.auth.presentation.screens.LoginScreen
import com.kazedev.wher_sbro.features.auth.presentation.screens.RegisterScreen
import com.kazedev.wher_sbro.features.auth.presentation.viewmodels.LoginViewModel
import com.kazedev.wher_sbro.features.auth.presentation.viewmodels.RegisterViewModel
// Importamos tu nueva pantalla del lobby
import com.kazedev.wher_sbro.features.radar.presentation.screens.LobbyScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RadarLobbyRoute // <-- CAMBIO 1: Ahora arranca directo en el Lobby
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
        composable<RadarLobbyRoute> {
            // <-- CAMBIO 2: Inyectamos la UI estática del lobby
            LobbyScreen()
        }
    }
}