package com.example.financexs.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingWelcome : NavKey

@Serializable
data object OnboardingProfile : NavKey

@Serializable
data object OnboardingAccounts : NavKey

@Serializable
data object OnboardingCategoriesExpense : NavKey

@Serializable
data object OnboardingCategoriesIncome : NavKey

@Serializable
data object OnboardingBudgets : NavKey

@Serializable
data object Home : NavKey

@Composable
fun OnboardingNavGraph(
    onComplete: () -> Unit = {}
) {
    val backStack = rememberNavBackStack(OnboardingWelcome)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<OnboardingWelcome> {
                // Placeholder - Se implementará en Bloque 1
                PlaceholderScreen("Welcome - Bloque 1")
            }
            entry<OnboardingProfile> {
                // Placeholder - Se implementará en Bloque 1
                PlaceholderScreen("Profile - Bloque 1")
            }
            entry<OnboardingAccounts> {
                // Placeholder - Se implementará en Bloque 2
                PlaceholderScreen("Accounts - Bloque 2")
            }
            entry<OnboardingCategoriesExpense> {
                // Placeholder - Se implementará en Bloque 3
                PlaceholderScreen("Categories Expense - Bloque 3")
            }
            entry<OnboardingCategoriesIncome> {
                // Placeholder - Se implementará en Bloque 4
                PlaceholderScreen("Categories Income - Bloque 4")
            }
            entry<OnboardingBudgets> {
                // Placeholder - Se implementará en Bloque 5
                PlaceholderScreen("Budgets - Bloque 5")
            }
            entry<Home> {
                LaunchedEffect(Unit) {
                    onComplete()
                }
            }
        }
    )
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}
