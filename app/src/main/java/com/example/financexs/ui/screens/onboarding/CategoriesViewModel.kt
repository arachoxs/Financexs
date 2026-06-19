package com.example.financexs.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.financexs.data.local.enums.TipoCategoria
import com.example.financexs.data.repository.CategoriaRepository
import com.example.financexs.domain.model.Categoria
import com.example.financexs.ui.components.coloresDisponibles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoriesUiState(
    val categoriasGasto: List<Categoria> = emptyList(),
    val categoriasIngreso: List<Categoria> = emptyList(),
    val tipoSeleccionado: TipoCategoria = TipoCategoria.GASTO,
    val showDialog: Boolean = false,
    val editingCategoria: Categoria? = null,
    val nombreCategoria: String = "",
    val colorSeleccionado: String = coloresDisponibles.first(),
    val iconoSeleccionado: String = "",
    val nombreError: String? = null,
    val iconoError: String? = null,
    val formError: String? = null,
    val isSaving: Boolean = false,
    val showDeleteConfirm: Categoria? = null
) {
    val categorias: List<Categoria>
        get() = if (tipoSeleccionado == TipoCategoria.GASTO) categoriasGasto else categoriasIngreso
}

class CategoriesViewModel(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoriaRepository.getCategoriasByTipo(TipoCategoria.GASTO).collect { categorias ->
                _uiState.update { it.copy(categoriasGasto = categorias) }
            }
        }
        viewModelScope.launch {
            categoriaRepository.getCategoriasByTipo(TipoCategoria.INGRESO).collect { categorias ->
                _uiState.update { it.copy(categoriasIngreso = categorias) }
            }
        }
    }

    fun selectTipo(tipo: TipoCategoria) {
        _uiState.update { it.copy(tipoSeleccionado = tipo) }
    }

    fun showNewCategoryForm() {
        _uiState.update {
            it.copy(
                showDialog = true,
                editingCategoria = null,
                nombreCategoria = "",
                colorSeleccionado = coloresDisponibles.first(),
                iconoSeleccionado = "",
                nombreError = null,
                iconoError = null,
                formError = null
            )
        }
    }

    fun showEditCategory(categoria: Categoria) {
        _uiState.update {
            it.copy(
                showDialog = true,
                editingCategoria = categoria,
                nombreCategoria = categoria.nombre,
                colorSeleccionado = categoria.color,
                iconoSeleccionado = categoria.icono,
                nombreError = null,
                iconoError = null,
                formError = null
            )
        }
    }

    fun dismissForm() {
        _uiState.update { it.copy(showDialog = false, editingCategoria = null, nombreError = null, iconoError = null, formError = null) }
    }

    fun updateNombre(nombre: String) {
        _uiState.update { it.copy(nombreCategoria = nombre, nombreError = null) }
    }

    fun updateColor(color: String) {
        _uiState.update { it.copy(colorSeleccionado = color) }
    }

    fun updateIcono(icono: String) {
        _uiState.update { it.copy(iconoSeleccionado = icono, iconoError = null) }
    }

    fun saveCategoria(onSuccess: () -> Unit) {
        val state = _uiState.value

        // Validate ALL fields — collect errors, don't return early
        var nombreError: String? = null
        var iconoError: String? = null

        if (state.nombreCategoria.trim().length < 2) {
            nombreError = "El nombre debe tener al menos 2 caracteres"
        }

        if (state.iconoSeleccionado.isEmpty()) {
            iconoError = "Selecciona un icono"
        }

        val nombreTrimmed = state.nombreCategoria.trim()
        val existsInSameType = state.categorias.any {
            it.nombre.equals(nombreTrimmed, ignoreCase = true) &&
                it.id != state.editingCategoria?.id
        }
        if (existsInSameType) {
            nombreError = "Ya existe una categoría con ese nombre"
        }

        // If any validation errors, show ALL and return
        if (nombreError != null || iconoError != null) {
            _uiState.update { it.copy(nombreError = nombreError, iconoError = iconoError, formError = null) }
            return
        }

        _uiState.update { it.copy(isSaving = true, nombreError = null, iconoError = null, formError = null) }

        viewModelScope.launch {
            try {
                val categoria = Categoria(
                    id = state.editingCategoria?.id ?: 0,
                    nombre = nombreTrimmed,
                    tipo = state.tipoSeleccionado,
                    color = state.colorSeleccionado,
                    icono = state.iconoSeleccionado
                )

                if (state.editingCategoria != null) {
                    categoriaRepository.updateCategoria(categoria)
                } else {
                    categoriaRepository.insertCategoria(categoria)
                }

                _uiState.update { it.copy(isSaving = false, showDialog = false, editingCategoria = null) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, formError = "Error al guardar. Intenta de nuevo.") }
            }
        }
    }

    fun requestDelete(categoria: Categoria) {
        _uiState.update { it.copy(showDeleteConfirm = categoria) }
    }

    fun confirmDelete() {
        val categoria = _uiState.value.showDeleteConfirm ?: return
        _uiState.update { it.copy(showDeleteConfirm = null) }

        viewModelScope.launch {
            try {
                categoriaRepository.deleteCategoria(categoria)
            } catch (e: Exception) {
                _uiState.update { it.copy(formError = "Error al eliminar. Intenta de nuevo.") }
            }
        }
    }

    fun dismissDeleteConfirm() {
        _uiState.update { it.copy(showDeleteConfirm = null) }
    }
}

class CategoriesViewModelFactory(
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(categoriaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
