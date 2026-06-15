package com.example.financexs.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class FinanceColors(
    val income: Color,
    val expense: Color,
    val error: Color
)

val LocalFinanceColors = staticCompositionLocalOf<FinanceColors> {
    error("No FinanceColors provided. Wrap your app with FinancexsTheme.")
}

internal val DarkFinanceColors = FinanceColors(
    income = FinanceIncomeDark,
    expense = FinanceExpenseDark,
    error = FinanceErrorDark
)

internal val LightFinanceColors = FinanceColors(
    income = FinanceIncomeLight,
    expense = FinanceExpenseLight,
    error = FinanceErrorLight
)
