package com.example.financexs.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.financexs.di.AppModule
import com.example.financexs.ui.screens.onboarding.AccountsViewModel
import com.example.financexs.ui.screens.onboarding.AccountsViewModelFactory
import com.example.financexs.ui.screens.onboarding.AccountsScreen
import com.example.financexs.ui.screens.onboarding.BudgetsViewModel
import com.example.financexs.ui.screens.onboarding.BudgetsViewModelFactory
import com.example.financexs.ui.screens.onboarding.BudgetsScreen
import com.example.financexs.ui.screens.onboarding.CategoriesViewModel
import com.example.financexs.ui.screens.onboarding.CategoriesViewModelFactory
import com.example.financexs.ui.screens.onboarding.CategoriesScreen
import com.example.financexs.ui.screens.onboarding.OnboardingViewModel
import com.example.financexs.ui.screens.onboarding.OnboardingViewModelFactory
import com.example.financexs.ui.screens.onboarding.ProfileScreen
import com.example.financexs.ui.screens.onboarding.WelcomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingWelcome : NavKey

@Serializable
data object OnboardingProfile : NavKey

@Serializable
data object OnboardingAccounts : NavKey

@Serializable
data object OnboardingCategories : NavKey

@Serializable
data object OnboardingBudgets : NavKey

@Serializable
data object Home : NavKey

@Composable
fun OnboardingNavGraph(
    onComplete: () -> Unit = {}
) {
    val backStack = rememberNavBackStack(OnboardingWelcome)
    val viewModel: OnboardingViewModel = viewModel(
        factory = OnboardingViewModelFactory(AppModule.perfilRepository)
    )

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(350)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(350)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(350)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(350)
            )
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(350)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(350)
            )
        },
        entryProvider = entryProvider {
            entry<OnboardingWelcome> {
                WelcomeScreen(
                    onStartClick = dropUnlessResumed {
                        backStack.add(OnboardingProfile)
                    }
                )
            }
            entry<OnboardingProfile> {
                val uiState by viewModel.uiState.collectAsState()

                ProfileScreen(
                    uiState = uiState,
                    onNombreChange = viewModel::updateNombre,
                    onMonedaSelect = viewModel::updateMoneda,
                    onTemaSelect = viewModel::updateTema,
                    onBackClick = dropUnlessResumed {
                        backStack.removeLastOrNull()
                    },
                    onNextClick = dropUnlessResumed {
                        viewModel.validateAndSave {
                            backStack.add(OnboardingAccounts)
                        }
                    }
                )
            }
            entry<OnboardingAccounts> {
                val accountsViewModel: AccountsViewModel = viewModel(
                    factory = AccountsViewModelFactory(AppModule.cuentaRepository)
                )
                val uiState by accountsViewModel.uiState.collectAsState()
                val moneda = viewModel.uiState.value.monedaSeleccionada.codigo

                AccountsScreen(
                    uiState = uiState,
                    onNewAccount = { accountsViewModel.showNewAccountForm(moneda) },
                    onEditAccount = accountsViewModel::showEditAccount,
                    onDeleteRequest = accountsViewModel::requestDelete,
                    onDismissForm = accountsViewModel::dismissForm,
                    onNombreChange = accountsViewModel::updateNombre,
                    onColorSelect = accountsViewModel::updateColor,
                    onIconSelect = accountsViewModel::updateIcono,
                    onSaldoChange = accountsViewModel::updateSaldo,
                    onSaveCuenta = {
                        accountsViewModel.saveCuenta {}
                    },
                    onConfirmDelete = accountsViewModel::confirmDelete,
                    onDismissDelete = accountsViewModel::dismissDeleteConfirm,
                    onBackClick = dropUnlessResumed {
                        backStack.removeLastOrNull()
                    },
                    onNextClick = dropUnlessResumed {
                        backStack.add(OnboardingCategories)
                    }
                )
            }
            entry<OnboardingCategories> {
                val categoriesViewModel: CategoriesViewModel = viewModel(
                    factory = CategoriesViewModelFactory(AppModule.categoriaRepository)
                )
                val uiState by categoriesViewModel.uiState.collectAsState()

                CategoriesScreen(
                    uiState = uiState,
                    onTipoSelect = categoriesViewModel::selectTipo,
                    onNewCategory = categoriesViewModel::showNewCategoryForm,
                    onEditCategory = categoriesViewModel::showEditCategory,
                    onDeleteRequest = categoriesViewModel::requestDelete,
                    onDismissForm = categoriesViewModel::dismissForm,
                    onNombreChange = categoriesViewModel::updateNombre,
                    onColorSelect = categoriesViewModel::updateColor,
                    onIconSelect = categoriesViewModel::updateIcono,
                    onSaveCategoria = { categoriesViewModel.saveCategoria {} },
                    onConfirmDelete = categoriesViewModel::confirmDelete,
                    onDismissDelete = categoriesViewModel::dismissDeleteConfirm,
                    onBackClick = dropUnlessResumed {
                        backStack.removeLastOrNull()
                    },
                    onNextClick = dropUnlessResumed {
                        backStack.add(OnboardingBudgets)
                    }
                )
            }
            entry<OnboardingBudgets> {
                val budgetsViewModel: BudgetsViewModel = viewModel(
                    factory = BudgetsViewModelFactory(AppModule.presupuestoRepository, AppModule.categoriaRepository)
                )
                val uiState by budgetsViewModel.uiState.collectAsState()

                BudgetsScreen(
                    uiState = uiState,
                    onNewBudget = { budgetsViewModel.showNewBudgetForm() },
                    onEditBudget = budgetsViewModel::showEditBudget,
                    onDeleteRequest = budgetsViewModel::requestDelete,
                    onDismissForm = budgetsViewModel::dismissForm,
                    onCategoriaSelect = budgetsViewModel::updateCategoria,
                    onLimiteChange = budgetsViewModel::updateLimite,
                    onPeriodoSelect = budgetsViewModel::updatePeriodo,
                    onSavePresupuesto = { budgetsViewModel.savePresupuesto {} },
                    onConfirmDelete = budgetsViewModel::confirmDelete,
                    onDismissDelete = budgetsViewModel::dismissDeleteConfirm,
                    onBackClick = dropUnlessResumed {
                        backStack.removeLastOrNull()
                    },
                    onNextClick = dropUnlessResumed {
                        backStack.add(Home)
                    }
                )
            }
            entry<Home> {
                LaunchedEffect(Unit) {
                    onComplete()
                }
            }
        }
    )
}
