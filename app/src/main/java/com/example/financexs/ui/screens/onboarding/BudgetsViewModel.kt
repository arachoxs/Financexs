package com.example.financexs.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.financexs.data.local.enums.PeriodoPresupuesto
import com.example.financexs.data.local.enums.TipoCategoria
import com.example.financexs.data.repository.CategoriaRepository
import com.example.financexs.data.repository.PresupuestoRepository
import com.example.financexs.domain.model.Categoria
import com.example.financexs.domain.model.Presupuesto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

data class BudgetsUiState(
    val presupuestos: List<Presupuesto> = emptyList(),
    val categoriasGasto: List<Categoria> = emptyList(),
    val showDialog: Boolean = false,
    val editingPresupuesto: Presupuesto? = null,
    val categoriaId: Long = 0,
    val limite: String = "",
    val periodo: PeriodoPresupuesto = PeriodoPresupuesto.MENSUAL,
    val categoriaError: String? = null,
    val limiteError: String? = null,
    val formError: String? = null,
    val isSaving: Boolean = false,
    val showDeleteConfirm: Presupuesto? = null
)

class BudgetsViewModel(
    private val presupuestoRepository: PresupuestoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetsUiState())
    val uiState: StateFlow<BudgetsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            presupuestoRepository.getAllPresupuestos().collect { presupuestos ->
                _uiState.update { it.copy(presupuestos = presupuestos) }
            }
        }
        viewModelScope.launch {
            categoriaRepository.getCategoriasByTipo(TipoCategoria.GASTO).collect { categorias ->
                _uiState.update { it.copy(categoriasGasto = categorias) }
            }
        }
    }

    fun showNewBudgetForm() {
        _uiState.update {
            it.copy(
                showDialog = true,
                editingPresupuesto = null,
                categoriaId = 0,
                limite = "",
                periodo = PeriodoPresupuesto.MENSUAL,
                categoriaError = null,
                limiteError = null,
                formError = null
            )
        }
    }

    fun showEditBudget(presupuesto: Presupuesto) {
        _uiState.update {
            it.copy(
                showDialog = true,
                editingPresupuesto = presupuesto,
                categoriaId = presupuesto.categoriaId,
                limite = presupuesto.limite.toPlainString(),
                periodo = presupuesto.periodo,
                categoriaError = null,
                limiteError = null,
                formError = null
            )
        }
    }

    fun dismissForm() {
        _uiState.update {
            it.copy(
                showDialog = false,
                editingPresupuesto = null,
                categoriaError = null,
                limiteError = null,
                formError = null
            )
        }
    }

    fun updateCategoria(categoriaId: Long) {
        _uiState.update { it.copy(categoriaId = categoriaId, categoriaError = null) }
    }

    fun updateLimite(limite: String) {
        _uiState.update { it.copy(limite = limite, limiteError = null) }
    }

    fun updatePeriodo(periodo: PeriodoPresupuesto) {
        _uiState.update { it.copy(periodo = periodo) }
    }

    fun savePresupuesto(onSuccess: () -> Unit) {
        val state = _uiState.value

        // Validate ALL fields — collect errors, don't return early
        var categoriaError: String? = null
        var limiteError: String? = null

        if (state.categoriaId == 0L) {
            categoriaError = "Selecciona una categoría"
        }

        val limite = state.limite.toBigDecimalOrNull()
        if (limite == null || limite <= BigDecimal.ZERO) {
            limiteError = "Ingresa un monto mayor a 0"
        }

        // If any validation errors, show ALL and return
        if (categoriaError != null || limiteError != null) {
            _uiState.update { it.copy(categoriaError = categoriaError, limiteError = limiteError, formError = null) }
            return
        }

        // Check unique constraint: (categoriaId, periodo)
        viewModelScope.launch {
            val exists = presupuestoRepository.existsByCategoriaAndPeriodo(
                categoriaId = state.categoriaId,
                periodo = state.periodo,
                excludeId = state.editingPresupuesto?.id ?: 0
            )
            if (exists) {
                val periodoLabel = when (state.periodo) {
                    PeriodoPresupuesto.DIARIO -> "Diario"
                    PeriodoPresupuesto.SEMANAL -> "Semanal"
                    PeriodoPresupuesto.MENSUAL -> "Mensual"
                }
                _uiState.update {
                    it.copy(formError = "Ya existe un presupuesto $periodoLabel para esta categoría")
                }
                return@launch
            }

            _uiState.update { it.copy(isSaving = true, categoriaError = null, limiteError = null, formError = null) }

            try {
                val presupuesto = Presupuesto(
                    id = state.editingPresupuesto?.id ?: 0,
                    categoriaId = state.categoriaId,
                    limite = limite!!,
                    periodo = state.periodo
                )

                if (state.editingPresupuesto != null) {
                    presupuestoRepository.updatePresupuesto(presupuesto)
                } else {
                    presupuestoRepository.insertPresupuesto(presupuesto)
                }

                _uiState.update { it.copy(isSaving = false, showDialog = false, editingPresupuesto = null) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, formError = "Error al guardar. Intenta de nuevo.") }
            }
        }
    }

    fun requestDelete(presupuesto: Presupuesto) {
        _uiState.update { it.copy(showDeleteConfirm = presupuesto) }
    }

    fun confirmDelete() {
        val presupuesto = _uiState.value.showDeleteConfirm ?: return
        _uiState.update { it.copy(showDeleteConfirm = null) }

        viewModelScope.launch {
            try {
                presupuestoRepository.deletePresupuesto(presupuesto)
            } catch (e: Exception) {
                _uiState.update { it.copy(formError = "Error al eliminar. Intenta de nuevo.") }
            }
        }
    }

    fun dismissDeleteConfirm() {
        _uiState.update { it.copy(showDeleteConfirm = null) }
    }
}

class BudgetsViewModelFactory(
    private val presupuestoRepository: PresupuestoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetsViewModel(presupuestoRepository, categoriaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
