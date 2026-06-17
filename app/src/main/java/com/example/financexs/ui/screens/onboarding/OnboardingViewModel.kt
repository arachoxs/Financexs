package com.example.financexs.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financexs.data.local.enums.TemaApp
import com.example.financexs.data.repository.PerfilRepository
import com.example.financexs.domain.model.Moneda
import com.example.financexs.domain.model.Perfil
import com.example.financexs.domain.model.monedasDisponibles
import com.example.financexs.ui.theme.ThemeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

data class OnboardingUiState(
    val nombre: String = "",
    val monedaSeleccionada: Moneda = monedasDisponibles.first(),
    val temaSeleccionado: TemaApp = TemaApp.SISTEMA,
    val nombreError: String? = null,
    val isSaving: Boolean = false
)

class OnboardingViewModel(
    private val perfilRepository: PerfilRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateNombre(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, nombreError = null) }
    }

    fun updateMoneda(moneda: Moneda) {
        _uiState.update { it.copy(monedaSeleccionada = moneda) }
    }

    fun updateTema(tema: TemaApp) {
        ThemeManager.updateTheme(tema)
        _uiState.update { it.copy(temaSeleccionado = tema) }
    }

    fun validateAndSave(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.nombre.trim().length < 2) {
            _uiState.update { it.copy(nombreError = "El nombre debe tener al menos 2 caracteres") }
            return
        }

        _uiState.update { it.copy(isSaving = true, nombreError = null) }

        viewModelScope.launch {
            val perfil = Perfil(
                id = 1,
                nombre = state.nombre.trim(),
                moneda = state.monedaSeleccionada.codigo,
                tema = state.temaSeleccionado
            )
            perfilRepository.savePerfil(perfil)
            _uiState.update { it.copy(isSaving = false) }
            onSuccess()
        }
    }
}

class OnboardingViewModelFactory(
    private val perfilRepository: PerfilRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OnboardingViewModel(perfilRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
