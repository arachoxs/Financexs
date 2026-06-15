package com.example.financexs.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = FinancePrimaryDark,
    onPrimary = FinanceOnPrimaryDark,
    primaryContainer = FinancePrimaryContainerDark,
    background = FinanceBackgroundDark,
    surface = FinanceSurfaceDark,
    surfaceVariant = FinanceSurfaceVariantDark,
    onSurface = FinanceOnSurfaceDark,
    onSurfaceVariant = FinanceOnSurfaceVariantDark,
    error = FinanceErrorDark,
    onError = FinanceOnErrorDark
)

private val LightColorScheme = lightColorScheme(
    primary = FinancePrimaryLight,
    onPrimary = FinanceOnPrimaryLight,
    primaryContainer = FinancePrimaryContainerLight,
    background = FinanceBackgroundLight,
    surface = FinanceSurfaceLight,
    surfaceVariant = FinanceSurfaceVariantLight,
    onSurface = FinanceOnSurfaceLight,
    onSurfaceVariant = FinanceOnSurfaceVariantLight,
    error = FinanceErrorLight,
    onError = FinanceOnErrorLight
)

@Composable
fun FinancexsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val financeColors = if (darkTheme) DarkFinanceColors else LightFinanceColors

    CompositionLocalProvider(LocalFinanceColors provides financeColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = FinanceTypography,
            content = content
        )
    }
}
