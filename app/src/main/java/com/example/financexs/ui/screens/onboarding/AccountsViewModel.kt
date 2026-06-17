package com.example.financexs.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.financexs.data.repository.CuentaRepository
import com.example.financexs.domain.model.Cuenta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

data class AccountsUiState(
    val cuentas: List<Cuenta> = emptyList(),
    val showDialog: Boolean = false,
    val editingCuenta: Cuenta? = null,
    val nombreCuenta: String = "",
    val colorSeleccionado: String = "#8B5CF6",
    val iconoSeleccionado: String = "AccountBalance",
    val saldoInicial: String = "",
    val moneda: String = "COP",
    val nombreError: String? = null,
    val isSaving: Boolean = false,
    val showDeleteConfirm: Cuenta? = null
)

class AccountsViewModel(
    private val cuentaRepository: CuentaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountsUiState())
    val uiState: StateFlow<AccountsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            cuentaRepository.getAllCuentas().collect { cuentas ->
                _uiState.update { it.copy(cuentas = cuentas) }
            }
        }
    }

    fun showNewAccountForm(moneda: String = "COP") {
        _uiState.update {
            it.copy(
                showDialog = true,
                editingCuenta = null,
                nombreCuenta = "",
                colorSeleccionado = "#8B5CF6",
                iconoSeleccionado = "AccountBalance",
                saldoInicial = "",
                moneda = moneda,
                nombreError = null
            )
        }
    }

    fun showEditAccount(cuenta: Cuenta) {
        _uiState.update {
            it.copy(
                showDialog = true,
                editingCuenta = cuenta,
                nombreCuenta = cuenta.nombre,
                colorSeleccionado = cuenta.color,
                iconoSeleccionado = cuenta.icono,
                saldoInicial = cuenta.saldo.toPlainString(),
                nombreError = null
            )
        }
    }

    fun dismissForm() {
        _uiState.update { it.copy(showDialog = false, editingCuenta = null, nombreError = null) }
    }

    fun updateNombre(nombre: String) {
        _uiState.update { it.copy(nombreCuenta = nombre, nombreError = null) }
    }

    fun updateColor(color: String) {
        _uiState.update { it.copy(colorSeleccionado = color) }
    }

    fun updateIcono(icono: String) {
        _uiState.update { it.copy(iconoSeleccionado = icono) }
    }

    fun updateSaldo(saldo: String) {
        _uiState.update { it.copy(saldoInicial = saldo) }
    }

    fun saveCuenta(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.nombreCuenta.trim().length < 2) {
            _uiState.update { it.copy(nombreError = "El nombre debe tener al menos 2 caracteres") }
            return
        }

        val saldo = state.saldoInicial.toBigDecimalOrNull() ?: BigDecimal.ZERO

        _uiState.update { it.copy(isSaving = true, nombreError = null) }

        viewModelScope.launch {
            try {
                val cuenta = Cuenta(
                    id = state.editingCuenta?.id ?: 0,
                    nombre = state.nombreCuenta.trim(),
                    color = state.colorSeleccionado,
                    icono = state.iconoSeleccionado,
                    saldo = saldo
                )

                if (state.editingCuenta != null) {
                    cuentaRepository.updateCuenta(cuenta)
                } else {
                    cuentaRepository.insertCuenta(cuenta)
                }

                _uiState.update { it.copy(isSaving = false, showDialog = false, editingCuenta = null) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    fun requestDelete(cuenta: Cuenta) {
        _uiState.update { it.copy(showDeleteConfirm = cuenta) }
    }

    fun confirmDelete() {
        val cuenta = _uiState.value.showDeleteConfirm ?: return
        _uiState.update { it.copy(showDeleteConfirm = null) }

        viewModelScope.launch {
            try {
                cuentaRepository.deleteCuenta(cuenta)
            } catch (e: Exception) {
                // Error handling - the cuenta remains in the list
            }
        }
    }

    fun dismissDeleteConfirm() {
        _uiState.update { it.copy(showDeleteConfirm = null) }
    }
}

class AccountsViewModelFactory(
    private val cuentaRepository: CuentaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountsViewModel(cuentaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
