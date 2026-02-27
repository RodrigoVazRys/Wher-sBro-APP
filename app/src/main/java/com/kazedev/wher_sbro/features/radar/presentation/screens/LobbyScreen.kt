package com.kazedev.wher_sbro.features.radar.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kazedev.wher_sbro.features.auth.presentation.components.TacticalInputField
import com.kazedev.wher_sbro.features.radar.presentation.components.RoomCodeDialog
import com.kazedev.wher_sbro.features.radar.presentation.viewmodels.LobbyViewModel

@Composable
fun LobbyScreen(
    onNavigateToRadar: (roomCode: String, targetName: String) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: LobbyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.roomCode) {
        val code = uiState.roomCode
        if (code.isNotBlank()) {
            val targetName = uiState.operatorName.ifBlank { "Target" }

            onNavigateToRadar(code, targetName)

            viewModel.clearRoomCode()
        }
    }

    LaunchedEffect(uiState.isSessionExpired) {
        if (uiState.isSessionExpired) {
            onNavigateToLogin()
        }
    }

    val terminalGreen = Color(0xFF1AFA82)
    val darkBg = Color(0xFF030C05)
    val fieldBg = Color(0xFF0A160D)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = null,
                        tint = terminalGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONNECTED",
                        color = terminalGreen,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            StaticRadar()
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = buildAnnotatedString {
                    append("WHER'S")
                    withStyle(style = SpanStyle(color = terminalGreen)) {
                        append("BRO")
                    }
                },
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ENCUENTRA A TU AMIGO",
                color = Color.Gray,
                letterSpacing = 1.sp,
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- NUEVO: Agregamos los campos de texto que faltaban ---
            TacticalInputField(
                value = uiState.operatorName,
                onValueChange = { }, // Es solo de lectura, el usuario no lo puede cambiar aquí
                label = "OPERATOR NAME",
                placeholder = "Cargando...",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(24.dp))

            TacticalInputField(
                value = uiState.frequencyCode,
                onValueChange = { viewModel.onFrequencyCodeChange(it) },
                label = "FREQUENCY CODE",
                placeholder = "# XXX-XXX",
                icon = Icons.Default.Numbers
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Crear Sala
            Button(
                onClick = viewModel::createRoom,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = terminalGreen)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("CREAR NUEVA SALA", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Unirse a Sala
            OutlinedButton(
                onClick = { viewModel.joinRoom() }, // <-- NUEVO: Llamamos a joinRoom() sin parámetros
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = fieldBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, terminalGreen.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = terminalGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("UNIRSE A SALA", color = terminalGreen, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("SYS: ONLINE", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                Text("LAT: 24MS", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                Text("REGION: MX-SOUTH", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
        }

        // Diálogo para mostrar el código de la sala
        if (uiState.roomCode.isNotBlank()) {
            RoomCodeDialog(
                roomCode = uiState.roomCode,
                onDismiss = { viewModel.clearRoomCode() }
            )
        }
    }
}

@Composable
fun StaticRadar() {
    val terminalGreen = Color(0xFF1AFA82)

    Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 2

            drawCircle(color = terminalGreen.copy(alpha = 0.15f), radius = radius, style = Stroke(1f))
            drawCircle(color = terminalGreen.copy(alpha = 0.15f), radius = radius * 0.66f, style = Stroke(1f))
            drawCircle(color = terminalGreen.copy(alpha = 0.15f), radius = radius * 0.33f, style = Stroke(1f))

            drawLine(color = terminalGreen.copy(alpha = 0.15f), start = Offset(center.x, 0f), end = Offset(center.x, size.height), strokeWidth = 1f)
            drawLine(color = terminalGreen.copy(alpha = 0.15f), start = Offset(0f, center.y), end = Offset(size.width, center.y), strokeWidth = 1f)

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        terminalGreen.copy(alpha = 0.5f)
                    ),
                    center = center
                ),
                startAngle = -120f,
                sweepAngle = 60f,
                useCenter = true
            )

            drawCircle(color = terminalGreen, radius = 5.dp.toPx(), center = center)

            drawCircle(color = Color(0xFFE53935), radius = 4.dp.toPx(), center = Offset(center.x - 40f, center.y - 50f))
            drawCircle(color = terminalGreen, radius = 4.dp.toPx(), center = Offset(center.x + 70f, center.y + 30f))
        }
    }
}