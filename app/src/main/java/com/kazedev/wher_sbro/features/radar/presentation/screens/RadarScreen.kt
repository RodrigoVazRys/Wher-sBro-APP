package com.kazedev.wher_sbro.features.radar.presentation.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarScreen(
    roomCode: String = "X7K-92L",
    targetName: String = "ViperTwo",
    onDisconnect: () -> Unit
) {
    val terminalGreen = Color(0xFF1AFA82)
    val darkBg = Color(0xFF030C05)
    val fieldBg = Color(0xFF0A160D)
    val alertRed = Color(0xFFE53935)

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
                        Canvas(modifier = Modifier.size(8.dp)) {
                            drawCircle(color = terminalGreen)
                        }
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

            Text(
                text = buildAnnotatedString {
                    append("Rastreando a: ")
                    withStyle(style = SpanStyle(color = terminalGreen, fontWeight = FontWeight.Bold)) {
                        append(targetName)
                    }
                },
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⟳ ACTUALIZANDO COORDENADAS...",
                color = terminalGreen.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.size(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.width / 2

                    drawCircle(color = terminalGreen.copy(alpha = 0.15f), radius = radius, style = Stroke(1.dp.toPx()))
                    drawCircle(color = terminalGreen.copy(alpha = 0.15f), radius = radius * 0.66f, style = Stroke(1.dp.toPx()))
                    drawCircle(color = terminalGreen.copy(alpha = 0.15f), radius = radius * 0.33f, style = Stroke(1.dp.toPx()))

                    drawLine(color = terminalGreen.copy(alpha = 0.15f), start = Offset(center.x, 0f), end = Offset(center.x, size.height))
                    drawLine(color = terminalGreen.copy(alpha = 0.15f), start = Offset(0f, center.y), end = Offset(size.width, center.y))

                    withTransform({
                        rotate(radarRotation, center)
                    }) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    terminalGreen.copy(alpha = 0.4f)
                                ),
                                center = center
                            ),
                            startAngle = -90f,
                            sweepAngle = 90f,
                            useCenter = true
                        )
                    }

                    drawCircle(color = terminalGreen, radius = 6.dp.toPx(), center = center)

                    val angleRad = Math.toRadians(300.0)
                    val arrowDistance = radius * 0.6f
                    val arrowX = center.x + (cos(angleRad) * arrowDistance).toFloat()
                    val arrowY = center.y + (sin(angleRad) * arrowDistance).toFloat()

                    withTransform({
                        rotate(300f + 90f, Offset(arrowX, arrowY))
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

                Text("N", color = terminalGreen, modifier = Modifier.align(Alignment.TopCenter).padding(8.dp), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                Text("S", color = terminalGreen, modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                Text("E", color = terminalGreen, modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                Text("W", color = terminalGreen, modifier = Modifier.align(Alignment.CenterStart).padding(8.dp), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fieldBg, RoundedCornerShape(16.dp))
                    .border(1.dp, terminalGreen.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(terminalGreen.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(20.dp)) {
                            drawRoundRect(color = terminalGreen, size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 12.dp.toPx()), topLeft = Offset(0f, 8.dp.toPx()))
                            drawRoundRect(color = terminalGreen, size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 16.dp.toPx()), topLeft = Offset(8.dp.toPx(), 4.dp.toPx()))
                            drawRoundRect(color = terminalGreen, size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 20.dp.toPx()), topLeft = Offset(16.dp.toPx(), 0f))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "DISTANCIA",
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "~5",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "metros",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                }

                Divider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.height(40.dp).width(1.dp)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SEÑAL",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Canvas(modifier = Modifier.size(width = 24.dp, height = 16.dp)) {
                        drawRoundRect(color = terminalGreen, size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 6.dp.toPx()), topLeft = Offset(0f, 10.dp.toPx()))
                        drawRoundRect(color = terminalGreen, size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 10.dp.toPx()), topLeft = Offset(6.dp.toPx(), 6.dp.toPx()))
                        drawRoundRect(color = terminalGreen, size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 14.dp.toPx()), topLeft = Offset(12.dp.toPx(), 2.dp.toPx()))
                        drawRoundRect(color = terminalGreen.copy(alpha = 0.3f), size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 16.dp.toPx()), topLeft = Offset(18.dp.toPx(), 0f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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