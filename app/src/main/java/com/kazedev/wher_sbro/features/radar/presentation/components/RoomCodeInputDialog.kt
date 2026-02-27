package com.kazedev.wher_sbro.features.radar.presentation.components


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RoomCodeInputDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val terminalGreen = Color(0xFF1AFA82)
    val darkBg = Color(0xFF030C05)
    val fieldBg = Color(0xFF0A160D)

    var code by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = darkBg,
        shape = RoundedCornerShape(8.dp),
        title = {
            Text(
                text = "UNIRSE A SALA",
                color = terminalGreen,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it.uppercase() },
                placeholder = {
                    Text("Ej: CSR2DV", color = Color.Gray, fontFamily = FontFamily.Monospace)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, terminalGreen.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = fieldBg,
                    unfocusedContainerColor = fieldBg,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = terminalGreen,
                    unfocusedTextColor = terminalGreen,
                    cursorColor = terminalGreen
                ),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (code.isNotBlank()) onConfirm(code)
                }
            ) {
                Text(
                    text = "UNIRSE",
                    color = terminalGreen,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "CANCELAR",
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    )
}