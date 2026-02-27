package com.kazedev.wher_sbro.features.auth.presentation.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kazedev.wher_sbro.R
import com.kazedev.wher_sbro.features.auth.presentation.components.FooterStat
import com.kazedev.wher_sbro.features.auth.presentation.components.HudCorners
import com.kazedev.wher_sbro.features.auth.presentation.components.TacticalInputField
import com.kazedev.wher_sbro.features.auth.presentation.viewmodels.RegisterUiState
import com.kazedev.wher_sbro.features.auth.presentation.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    // Recolectamos el nuevo estado del username
    val username by viewModel.username.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val terminalGreen = Color(0xFF1AFA82)
    val darkBg = Color(0xFF030C05)

    val infiniteTransition = rememberInfiniteTransition(label = "flicker")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
            onRegisterSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkBg
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            HudCorners(color = terminalGreen.copy(alpha = 0.5f))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "INITIALIZING NEW USER PROTOCOL V3.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = terminalGreen.copy(alpha = alpha),
                    fontFamily = FontFamily.Monospace
                )

                Image(
                    painter = painterResource(R.drawable.logo_wher_sbro_removebg),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .offset(y = (-30).dp)
                        .fillMaxWidth()
                        .height(280.dp)
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(placeable.width, placeable.height - 160) {
                                placeable.placeRelative(0, -40)
                            }
                        },
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "REGISTRO",
                    color = terminalGreen,
                    letterSpacing = 4.sp,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(placeable.width, 0) {
                                placeable.placeRelative(0, -180)
                            }
                        }
                )

                Spacer(modifier = Modifier.height(2.dp))

                // --- NUEVO CAMPO: NOMBRE DE USUARIO ---
                TacticalInputField(
                    value = username,
                    onValueChange = { viewModel.onUsernameChanged(it) },
                    label = "NOMBRE DE OPERADOR",
                    placeholder = "Usuario",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- CAMPO: EMAIL ---
                TacticalInputField(
                    value = email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = "CORREO ELECTRÓNICO", // Etiqueta ajustada
                    placeholder = "ejemplo@correo.com",
                    icon = Icons.Default.Email
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- CAMPO: CONTRASEÑA ---
                TacticalInputField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = "CONTRASEÑA",
                    placeholder = "Contraseña secreta",
                    icon = Icons.Default.Key,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.onRegister() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = uiState !is RegisterUiState.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = terminalGreen)
                ) {
                    if (uiState is RegisterUiState.Loading) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            "REGISTRARSE ➔",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿Ya tienes una cuenta?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Text(
                    text = "Ir al Login ↗",
                    color = terminalGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FooterStat("STATUS", "READY")
                    FooterStat("REGION", "MX-CH")
                    FooterStat("ENCRYP", "XYZ-2026")
                }
            }
        }
    }
}