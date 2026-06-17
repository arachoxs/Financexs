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
    val titleAlpha = remember { Animatable(0f) }
    val descAlpha = remember { Animatable(0f) }
    val buttonAlpha = remember { Animatable(0f) }
    val bgAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        bgAlpha.animateTo(1f, animationSpec = tween(1000))
        titleAlpha.animateTo(1f, animationSpec = tween(400, delayMillis = 200))
        descAlpha.animateTo(1f, animationSpec = tween(400, delayMillis = 300))
        buttonAlpha.animateTo(1f, animationSpec = tween(400, delayMillis = 500))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 48.dp)
    ) {
        BauhausBackground(bgAlpha)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "FINANCEXS",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 4.sp,
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Vamos a personalizar\n tu experiencia financiera",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(descAlpha.value)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .height(50.dp)
                    .alpha(buttonAlpha.value),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Comenzar",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun BauhausBackground(alpha: Animatable<Float, *>) {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val primary = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val a = alpha.value

        drawCircle(
            color = primaryContainer,
            radius = 160.dp.toPx(),
            center = Offset(width * 0.78f, height * 0.18f)
        )

        drawCircle(
            color = primary.copy(alpha = 0.06f * a),
            radius = 200.dp.toPx(),
            center = Offset(width * 0.78f, height * 0.18f)
        )

        drawCircle(
            color = primaryContainer,
            radius = 60.dp.toPx(),
            center = Offset(width * 0.22f, height * 0.35f)
        )

        drawCircle(
            color = primary.copy(alpha = 0.08f * a),
            radius = 32.dp.toPx(),
            center = Offset(width * 0.65f, height * 0.42f)
        )

        drawCircle(
            color = primary.copy(alpha = 0.04f * a),
            radius = 16.dp.toPx(),
            center = Offset(width * 0.15f, height * 0.22f)
        )

        drawLine(
            color = primary.copy(alpha = 0.1f * a),
            start = Offset(width * 0.05f, height * 0.32f),
            end = Offset(width * 0.45f, height * 0.32f),
            strokeWidth = 1.5.dp.toPx()
        )

        drawLine(
            color = primary.copy(alpha = 0.06f * a),
            start = Offset(width * 0.72f, height * 0.05f),
            end = Offset(width * 0.92f, height * 0.48f),
            strokeWidth = 1.dp.toPx()
        )

        val arcPath = Path().apply {
            moveTo(width * 0.5f, height * 0.5f)
            quadraticTo(
                width * 0.85f, height * 0.08f,
                width * 0.98f, height * 0.35f
            )
        }
        drawPath(
            path = arcPath,
            color = primary.copy(alpha = 0.05f * a),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
        )
    }
}
