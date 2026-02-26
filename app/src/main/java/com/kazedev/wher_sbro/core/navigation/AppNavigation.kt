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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute
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
                    // Si el registro es exitoso, navegamos al home y borramos el login del historial
                    navController.navigate(RadarLobbyRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    // Simplemente sacamos la pantalla de registro de la pila para volver al Login
                    navController.popBackStack()
                }
            )
        }

        // --- 3. PANTALLA PRINCIPAL (LOBBY) ---
        composable<RadarLobbyRoute> {
            // TODO: Aquí colocarás tu pantalla del mapa/radar cuando la tengas lista.
            // Por ahora puedes poner un Text temporal para que no crashee si logras entrar.
        }
    }
}