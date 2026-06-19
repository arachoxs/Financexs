package com.example.financexs.ui.screens.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financexs.ui.theme.BricolageGrotesque
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Background animation
    val backgroundAlpha = remember { Animatable(0f) }

    // Illustration animations — staggered
    val outerCircleScale = remember { Animatable(0f) }
    val midCircleScale = remember { Animatable(0f) }
    val innerCircleScale = remember { Animatable(0f) }
    val dotScale = remember { Animatable(0f) }
    val linesProgress = remember { Animatable(0f) }
    val triangleRotation = remember { Animatable(-30f) }
    val triangleAlpha = remember { Animatable(0f) }

    // Text animations
    val titleAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(24f) }
    val descAlpha = remember { Animatable(0f) }
    val descOffsetY = remember { Animatable(20f) }

    // Button animation
    val buttonAlpha = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        // ── Background (0ms — fades in smoothly) ──
        launch { backgroundAlpha.animateTo(1f, tween(600, easing = LinearEasing)) }

        // ── Illustration: cascada de círculos (0ms → 400ms) ──
        launch {
            outerCircleScale.animateTo(
                1f,
                spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
            )
        }
        launch {
            midCircleScale.animateTo(
                1f,
                spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow)
            )
        }
        launch {
            innerCircleScale.animateTo(
                1f,
                spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium)
            )
        }
        launch {
            dotScale.animateTo(
                1f,
                spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
            )
        }

        // ── Illustration: líneas se dibujan (200ms → 600ms) ──
        launch {
            linesProgress.animateTo(1f, tween(500, delayMillis = 200, easing = FastOutSlowInEasing))
        }

        // ── Illustration: triángulo (300ms → 700ms) ──
        launch {
            triangleAlpha.animateTo(0.1f, tween(400, delayMillis = 300))
        }
        launch {
            triangleRotation.animateTo(0f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessVeryLow))
        }

        // ── Title (400ms) ──
        launch {
            titleAlpha.animateTo(1f, tween(350, delayMillis = 400, easing = FastOutSlowInEasing))
        }
        launch {
            titleOffsetY.animateTo(0f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow))
        }

        // ── Description (550ms) ──
        launch {
            descAlpha.animateTo(1f, tween(350, delayMillis = 550, easing = FastOutSlowInEasing))
        }
        launch {
            descOffsetY.animateTo(0f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow))
        }

        // ── Button (700ms) ──
        launch {
            buttonAlpha.animateTo(1f, tween(250, delayMillis = 700))
        }
        launch {
            buttonScale.animateTo(1f, spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Parallax background — independent animation
        BauhausBackground(alpha = backgroundAlpha.value)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Bauhaus Illustration (animated elements) ──
            BauhausIllustration(
                outerScale = outerCircleScale.value,
                midScale = midCircleScale.value,
                innerScale = innerCircleScale.value,
                dotScale = dotScale.value,
                linesProgress = linesProgress.value,
                triangleAlpha = triangleAlpha.value,
                triangleRotation = triangleRotation.value,
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ── Title: FINANCEXS ──
            Text(
                text = "FINANCEXS",
                fontFamily = BricolageGrotesque,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                letterSpacing = 6.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = titleAlpha.value
                        translationY = with(density) { titleOffsetY.value.dp.toPx() }
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Description ──
            Text(
                text = "Vamos a personalizar\n tu experiencia financiera",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = descAlpha.value
                        translationY = with(density) { descOffsetY.value.dp.toPx() }
                    }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ── Button: Comenzar (spring physics) ──
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp)
                    .graphicsLayer {
                        alpha = buttonAlpha.value
                        scaleX = buttonScale.value
                        scaleY = buttonScale.value
                    },
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
private fun BauhausIllustration(
    outerScale: Float,
    midScale: Float,
    innerScale: Float,
    dotScale: Float,
    linesProgress: Float,
    triangleAlpha: Float,
    triangleRotation: Float,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val surface = MaterialTheme.colorScheme.surface

    Canvas(modifier = modifier.graphicsLayer {
        rotationZ = triangleRotation * 0.1f // subtle global rotation
    }) {
        val width = size.width
        val height = size.height
        val cx = width / 2
        val cy = height / 2

        // ── Outer circle (scales in with spring) ──
        drawCircle(
            color = primaryContainer,
            radius = 70.dp.toPx() * outerScale,
            center = Offset(cx, cy)
        )

        // ── Mid circle (staggered) ──
        drawCircle(
            color = primary.copy(alpha = 0.15f),
            radius = 50.dp.toPx() * midScale,
            center = Offset(cx, cy)
        )

        // ── Inner circle ──
        drawCircle(
            color = primary.copy(alpha = 0.3f),
            radius = 30.dp.toPx() * innerScale,
            center = Offset(cx, cy)
        )

        // ── Center dot ──
        drawCircle(
            color = surface,
            radius = 12.dp.toPx() * dotScale,
            center = Offset(cx, cy)
        )

        // ── Horizontal line (draws progressively) ──
        val lineHalf = 80.dp.toPx()
        val drawnLength = lineHalf * linesProgress
        drawLine(
            color = primary.copy(alpha = 0.25f),
            start = Offset(cx - drawnLength, cy),
            end = Offset(cx + drawnLength, cy),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // ── Vertical line (draws progressively, delayed) ──
        val verticalProgress = (linesProgress - 0.3f).coerceIn(0f, 1f) / 0.7f
        val drawnVertical = lineHalf * verticalProgress
        drawLine(
            color = primary.copy(alpha = 0.2f),
            start = Offset(cx, cy - drawnVertical),
            end = Offset(cx, cy + drawnVertical),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // ── Triangle (rotates in with alpha) ──
        if (triangleAlpha > 0f) {
            val triSize = 45.dp.toPx()
            val path = Path().apply {
                moveTo(cx, cy - triSize)
                lineTo(cx + triSize * 0.87f, cy + triSize * 0.5f)
                lineTo(cx - triSize * 0.87f, cy + triSize * 0.5f)
                close()
            }
            drawPath(
                path = path,
                color = primary.copy(alpha = triangleAlpha)
            )
        }
    }
}

@Composable
private fun BauhausBackground(
    alpha: Float
) {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val primary = MaterialTheme.colorScheme.primary

    // Subtle parallax offset — elements drift slightly as they fade in
    val parallaxOffset = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        parallaxOffset.animateTo(15f, tween(1200, easing = LinearEasing))
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val px = parallaxOffset.value

        // Top-right circle — drifts down slightly
        drawCircle(
            color = primaryContainer.copy(alpha = 0.4f * alpha),
            radius = 200.dp.toPx(),
            center = Offset(width * 0.85f, height * 0.15f + px)
        )

        // Bottom-left circle — drifts up slightly
        drawCircle(
            color = primary.copy(alpha = 0.03f * alpha),
            radius = 120.dp.toPx(),
            center = Offset(width * 0.15f, height * 0.85f - px * 0.5f)
        )

        // Top-left line
        drawLine(
            color = primary.copy(alpha = 0.05f * alpha),
            start = Offset(0f, height * 0.25f),
            end = Offset(width * 0.3f, height * 0.25f),
            strokeWidth = 1.dp.toPx()
        )

        // Diagonal line — top-right to bottom
        drawLine(
            color = primary.copy(alpha = 0.04f * alpha),
            start = Offset(width * 0.7f, 0f),
            end = Offset(width, height * 0.4f),
            strokeWidth = 1.dp.toPx()
        )

        // Small decorative circle — mid-right
        drawCircle(
            color = primary.copy(alpha = 0.02f * alpha),
            radius = 40.dp.toPx(),
            center = Offset(width * 0.9f, height * 0.55f + px * 0.3f)
        )
    }
}
