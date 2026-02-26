package com.kazedev.wher_sbro.features.auth.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HudCorners(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        val lineLength = 30.dp.toPx()
        val strokeWidth = 1.5.dp.toPx()

        drawLine(color, Offset(0f, 0f), Offset(lineLength, 0f), strokeWidth)
        drawLine(color, Offset(0f, 0f), Offset(0f, lineLength), strokeWidth)

        drawLine(color, Offset(size.width, 0f), Offset(size.width - lineLength, 0f), strokeWidth)
        drawLine(color, Offset(size.width, 0f), Offset(size.width, lineLength), strokeWidth)

        drawLine(color, Offset(0f, size.height), Offset(lineLength, size.height), strokeWidth)
        drawLine(color, Offset(0f, size.height), Offset(0f, size.height - lineLength), strokeWidth)

        drawLine(color, Offset(size.width, size.height), Offset(size.width - lineLength, size.height), strokeWidth)
        drawLine(color, Offset(size.width, size.height), Offset(size.width, size.height - lineLength), strokeWidth)
    }
}

@Composable
fun TacticalInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    val terminalGreen = Color(0xFF1AFA82)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = terminalGreen,
            style = MaterialTheme.typography.labelMedium,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF0A160D),
                unfocusedContainerColor = Color(0xFF0A160D),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = terminalGreen,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            placeholder = { Text(placeholder, color = Color.Gray) },
            leadingIcon = { Icon(icon, contentDescription = null, tint = Color.Gray) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
        )
    }
}

@Composable
fun FooterStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value, color = Color(0xFF1AFA82), fontSize = 12.sp)
    }
}