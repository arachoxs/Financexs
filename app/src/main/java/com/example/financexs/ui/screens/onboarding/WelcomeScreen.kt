package com.example.financexs.ui.screens.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {
    val illustrationScale = remember { Animatable(0.6f) }
    val illustrationAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleY = remember { Animatable(30f) }
    val descAlpha = remember { Animatable(0f) }
    val descY = remember { Animatable(20f) }
    val buttonAlpha = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        illustrationScale.animateTo(1f, animationSpec = tween(400))
        illustrationAlpha.animateTo(1f, animationSpec = tween(300))
        titleAlpha.animateTo(1f, animationSpec = tween(250, delayMillis = 100))
        titleY.animateTo(0f, animationSpec = tween(250, delayMillis = 100))
        descAlpha.animateTo(1f, animationSpec = tween(250, delayMillis = 180))
        descY.animateTo(0f, animationSpec = tween(250, delayMillis = 180))
        buttonAlpha.animateTo(1f, animationSpec = tween(250, delayMillis = 250))
        buttonScale.animateTo(1f, animationSpec = tween(250, delayMillis = 250))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BauhausBackground(
            alpha = illustrationAlpha,
            scale = illustrationScale
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BauhausIllustration(
                modifier = Modifier
                    .size(180.dp)
                    .alpha(illustrationAlpha.value)
                    .scale(illustrationScale.value)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "FINANCEXS",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 4.sp,
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offsetY(titleY.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Vamos a personalizar\n tu experiencia financiera",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(descAlpha.value)
                    .offsetY(descY.value)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp)
                    .alpha(buttonAlpha.value)
                    .scale(buttonScale.value),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Comenzar",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun BauhausIllustration(modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val surface = MaterialTheme.colorScheme.surface

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2
        val centerY = height / 2

        drawCircle(
            color = primaryContainer,
            radius = 70.dp.toPx(),
            center = Offset(centerX, centerY)
        )

        drawCircle(
            color = primary.copy(alpha = 0.15f),
            radius = 50.dp.toPx(),
            center = Offset(centerX, centerY)
        )

        drawCircle(
            color = primary.copy(alpha = 0.3f),
            radius = 30.dp.toPx(),
            center = Offset(centerX, centerY)
        )

        drawCircle(
            color = surface,
            radius = 12.dp.toPx(),
            center = Offset(centerX, centerY)
        )

        drawLine(
            color = primary.copy(alpha = 0.2f),
            start = Offset(centerX - 80.dp.toPx(), centerY),
            end = Offset(centerX + 80.dp.toPx(), centerY),
            strokeWidth = 2.dp.toPx()
        )

        drawLine(
            color = primary.copy(alpha = 0.15f),
            start = Offset(centerX, centerY - 80.dp.toPx()),
            end = Offset(centerX, centerY + 80.dp.toPx()),
            strokeWidth = 2.dp.toPx()
        )

        val trianglePath = Path().apply {
            moveTo(centerX, centerY - 45.dp.toPx())
            lineTo(centerX + 40.dp.toPx(), centerY + 25.dp.toPx())
            lineTo(centerX - 40.dp.toPx(), centerY + 25.dp.toPx())
            close()
        }
        drawPath(
            path = trianglePath,
            color = primary.copy(alpha = 0.08f)
        )
    }
}

@Composable
private fun BauhausBackground(
    alpha: Animatable<Float, *>,
    scale: Animatable<Float, *>
) {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val primary = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val a = alpha.value
        val s = scale.value

        drawCircle(
            color = primaryContainer.copy(alpha = 0.5f * a),
            radius = 200.dp.toPx() * s,
            center = Offset(width * 0.85f, height * 0.15f)
        )

        drawCircle(
            color = primary.copy(alpha = 0.04f * a),
            radius = 120.dp.toPx() * s,
            center = Offset(width * 0.15f, height * 0.85f)
        )

        drawLine(
            color = primary.copy(alpha = 0.06f * a),
            start = Offset(0f, height * 0.25f),
            end = Offset(width * 0.3f, height * 0.25f),
            strokeWidth = 1.dp.toPx()
        )

        drawLine(
            color = primary.copy(alpha = 0.04f * a),
            start = Offset(width * 0.7f, 0f),
            end = Offset(width, height * 0.4f),
            strokeWidth = 1.dp.toPx()
        )
    }
}

private fun Modifier.offsetY(value: Float): Modifier {
    return this.then(
        Modifier.padding(top = value.dp)
    )
}
