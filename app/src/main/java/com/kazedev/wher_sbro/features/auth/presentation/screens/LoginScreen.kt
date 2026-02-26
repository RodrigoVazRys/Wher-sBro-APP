package com.kazedev.wher_sbro.features.auth.presentation.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kazedev.wher_sbro.R
import com.kazedev.wher_sbro.features.auth.presentation.viewmodels.LoginViewModel

import com.kazedev.wher_sbro.features.auth.presentation.components.HudCorners
import com.kazedev.wher_sbro.features.auth.presentation.components.TacticalInputField
import com.kazedev.wher_sbro.features.auth.presentation.components.FooterStat

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
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
                    text = "SECURE AUTH PROTOCOL V9.2",
                    style = MaterialTheme.typography.labelMedium,
                    color = terminalGreen.copy(alpha = alpha),
                    fontFamily = FontFamily.Monospace
                )

                Image(
                    painter = painterResource(R.drawable.logo_wher_sbro_removebg),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .offset(y = (-30).dp)
                        .fillMaxWidth()
                        .height(320.dp)
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(placeable.width, placeable.height - 180) {
                                placeable.placeRelative(0, -40)
                            }
                        },
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "FRIEND FINDER",
                    color = terminalGreen,
                    letterSpacing = 4.sp,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(placeable.width, 0) {
                                placeable.placeRelative(0, -210)
                            }
                        }
                )

                TacticalInputField(
                    value = email,
                    onValueChange = viewModel::onEmailChanged,
                    label = "IDENTIFICACIÓN",
                    placeholder = "Email / Usuario",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(24.dp))


                TacticalInputField(
                    value = password,
                    onValueChange = viewModel::onPasswordChanged,
                    label = "CREDENCIAL DE ACCESO",
                    placeholder = "Contraseña",
                    icon = Icons.Default.Key,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = viewModel::onLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = terminalGreen)
                ) {
                    Text(
                        "INICIAR SESIÓN ➔",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("¿Sin credenciales?", color = Color.Gray)
                Text(
                    "Crear cuenta ↗",
                    color = terminalGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FooterStat("SERVER", "ONLINE")
                    FooterStat("PING", "24ms")
                    FooterStat("ENCRYP", "AES-256")
                }
            }
        }
    }
}