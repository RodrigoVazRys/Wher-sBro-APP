package com.kazedev.wher_sbro.features.radar.presentation.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kazedev.wher_sbro.features.radar.presentation.viewmodels.RadarViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarScreen(
    roomCode: String,
    targetName: String,
    onDisconnect: () -> Unit,
    viewModel: RadarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            viewModel.initTracking(roomCode, targetName)
        } else {
            Toast.makeText(
                context,
                "⚠️ ACCESO DENEGADO: Se necesita acceso al GPS para funcionar.",
                Toast.LENGTH_LONG
            ).show()
            onDisconnect()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Paleta de colores
    val terminalGreen = Color(0xFF1AFA82)
    val darkBg = Color(0xFF030C05)
    val fieldBg = Color(0xFF0A160D)
    val alertRed = Color(0xFFE53935)

    // Animación de barrido del radar (360 grados)
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val radarRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarRotation"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- TOP BAR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDisconnect) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = terminalGreen)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "WHER-SBRO LOCATOR",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier.size(8.dp)) { drawCircle(color = terminalGreen) }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ROOM: $roomCode",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(onClick = { }) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- CABECERA DE OBJETIVO DINÁMICA ---
            Text(
                text = buildAnnotatedString {
                    if (uiState.isFriendConnected) {
                        append("Rastreando a: ")
                        withStyle(style = SpanStyle(color = terminalGreen, fontWeight = FontWeight.Bold)) {
                            append(uiState.targetName)
                        }
                    } else {
                        withStyle(style = SpanStyle(color = Color.Gray)) {
                            append("Buscando compañero de escuadrón...")
                        }
                    }
                },
                color = Color.White, fontSize = 20.sp, fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Texto dinámico de estado (Cargando, Conectado o Error)
            Text(
                text = uiState.error ?: if(uiState.isLoading) "⟳ ESPERANDO SEÑAL GPS..." else "⟳ CONEXIÓN ESTABLECIDA",
                color = if(uiState.error != null) alertRed else terminalGreen.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- EL RADAR TÁCTICO EN CANVAS ---
            Box(
                modifier = Modifier.size(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.width / 2

                    // Anillos concéntricos
                    drawCircle(color = terminalGreen.copy(alpha = 0.15f), radius = radius, style = Stroke(1.dp.toPx()))
                    drawCircle(color = terminalGreen.copy(alpha = 0.15f), radius = radius * 0.66f, style = Stroke(1.dp.toPx()))
                    drawCircle(color = terminalGreen.copy(alpha = 0.15f), radius = radius * 0.33f, style = Stroke(1.dp.toPx()))

                    // Líneas de ejes (Cruz)
                    drawLine(color = terminalGreen.copy(alpha = 0.15f), start = Offset(center.x, 0f), end = Offset(center.x, size.height))
                    drawLine(color = terminalGreen.copy(alpha = 0.15f), start = Offset(0f, center.y), end = Offset(size.width, center.y))

                    // Barrido del radar rotando
                    withTransform({ rotate(radarRotation, center) }) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                colors = listOf(Color.Transparent, Color.Transparent, terminalGreen.copy(alpha = 0.4f)),
                                center = center
                            ),
                            startAngle = -90f, sweepAngle = 90f, useCenter = true
                        )
                    }

                    // Punto central (Tú - siempre visible)
                    drawCircle(color = terminalGreen, radius = 6.dp.toPx(), center = center)

                    // Solo dibujamos a tu amigo si la carga inicial ya pasó Y está conectado
                    if (uiState.isFriendConnected && !uiState.isLoading) {
                        // Conversión polar a cartesiana.
                        // Restamos 90° porque en el Canvas de Android, 0° está a la derecha (Este).
                        val angleRad = Math.toRadians((uiState.bearingDegrees - 90).toDouble())

                        // Escalamos la distancia visual para que no se salga de la pantalla
                        val mappedDistance = (uiState.distanceMeters.toFloat() / 100f).coerceIn(0.2f, 0.9f)
                        val arrowDistance = radius * mappedDistance

                        val arrowX = center.x + (cos(angleRad) * arrowDistance).toFloat()
                        val arrowY = center.y + (sin(angleRad) * arrowDistance).toFloat()

                        // Dibujamos el triángulo direccional de tu amigo
                        withTransform({
                            rotate(uiState.bearingDegrees, Offset(arrowX, arrowY))
                        }) {
                            val path = Path().apply {
                                moveTo(arrowX, arrowY - 15.dp.toPx())
                                lineTo(arrowX - 10.dp.toPx(), arrowY + 10.dp.toPx())
                                lineTo(arrowX + 10.dp.toPx(), arrowY + 10.dp.toPx())
                                close()
                            }
                            drawPath(path = path, color = terminalGreen)
                        }
                    }
                }

                // Puntos cardinales
                Text("N", color = terminalGreen, modifier = Modifier.align(Alignment.TopCenter).padding(8.dp), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                Text("S", color = terminalGreen, modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                Text("E", color = terminalGreen, modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                Text("W", color = terminalGreen, modifier = Modifier.align(Alignment.CenterStart).padding(8.dp), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- ESTADÍSTICAS DEL PIE DE PÁGINA ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fieldBg, RoundedCornerShape(16.dp))
                    .border(1.dp, terminalGreen.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Distancia
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(40.dp).background(terminalGreen.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(20.dp)) {
                            drawRoundRect(color = terminalGreen, size = Size(4.dp.toPx(), 12.dp.toPx()), topLeft = Offset(0f, 8.dp.toPx()))
                            drawRoundRect(color = terminalGreen, size = Size(4.dp.toPx(), 16.dp.toPx()), topLeft = Offset(8.dp.toPx(), 4.dp.toPx()))
                            drawRoundRect(color = terminalGreen, size = Size(4.dp.toPx(), 20.dp.toPx()), topLeft = Offset(16.dp.toPx(), 0f))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("DISTANCIA", color = Color.Gray, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                // Valor dinámico: Si no hay amigo, "--". Si hay, la distancia en metros.
                                text = if(!uiState.isFriendConnected || uiState.isLoading) "--" else "~${uiState.distanceMeters}",
                                color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("metros", color = Color.Gray, fontSize = 14.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(bottom = 2.dp))
                        }
                    }
                }

                Divider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.height(40.dp).width(1.dp)
                )

                // Señal
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SEÑAL",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Canvas(modifier = Modifier.size(width = 24.dp, height = 16.dp)) {
                        drawRoundRect(color = terminalGreen, size = Size(4.dp.toPx(), 6.dp.toPx()), topLeft = Offset(0f, 10.dp.toPx()))
                        drawRoundRect(color = terminalGreen, size = Size(4.dp.toPx(), 10.dp.toPx()), topLeft = Offset(6.dp.toPx(), 6.dp.toPx()))
                        drawRoundRect(color = terminalGreen, size = Size(4.dp.toPx(), 14.dp.toPx()), topLeft = Offset(12.dp.toPx(), 2.dp.toPx()))
                        drawRoundRect(color = terminalGreen.copy(alpha = 0.3f), size = Size(4.dp.toPx(), 16.dp.toPx()), topLeft = Offset(18.dp.toPx(), 0f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTÓN DE DESCONEXIÓN ---
            Button(
                onClick = onDisconnect,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(1.dp, alertRed.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = alertRed.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = null,
                    tint = alertRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FINALIZAR CONEXIÓN",
                    color = alertRed,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "WHER-SBRO V2.4.0 • SECURE PROTOCOL",
                color = Color.Gray,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}