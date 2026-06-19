package com.example.financexs.ui.screens.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.financexs.data.local.enums.TipoCategoria
import com.example.financexs.domain.model.Categoria
import com.example.financexs.ui.components.AddItemCard
import com.example.financexs.ui.components.CategoryFormContent
import com.example.financexs.ui.components.CategoryFormState
import com.example.financexs.ui.components.CategoryTypeToggle
import com.example.financexs.ui.components.FormBottomSheet
import com.example.financexs.ui.components.IconOption
import com.example.financexs.ui.components.ItemCard
import com.example.financexs.ui.components.iconosCategoriasGasto
import com.example.financexs.ui.components.iconosCategoriasIngreso

@Composable
fun CategoriesScreen(
    uiState: CategoriesUiState,
    onTipoSelect: (TipoCategoria) -> Unit,
    onNewCategory: () -> Unit,
    onEditCategory: (Categoria) -> Unit,
    onDeleteRequest: (Categoria) -> Unit,
    onDismissForm: () -> Unit,
    onNombreChange: (String) -> Unit,
    onColorSelect: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onSaveCategoria: () -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val icons = if (uiState.tipoSeleccionado == TipoCategoria.GASTO) iconosCategoriasGasto else iconosCategoriasIngreso
    val categorias = uiState.categorias
    val emptyText = if (uiState.tipoSeleccionado == TipoCategoria.GASTO) "Gastos" else "Ingresos"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Tus Categorías",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Configura las categorías para tus movimientos",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            CategoryTypeToggle(
                selected = uiState.tipoSeleccionado,
                onSelect = onTipoSelect
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${categorias.size} categoría${if (categorias.size != 1) "s" else ""} de $emptyText",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (categorias.isEmpty()) {
                EmptyCategoriesState(
                    tipo = emptyText,
                    onNewCategory = onNewCategory
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(categorias) { index, categoria ->
                        ItemCard(
                            nombre = categoria.nombre,
                            colorHex = categoria.color,
                            icon = icons.find { it.name == categoria.icono }?.icon
                                ?: icons.first().icon,
                            subtitulo = null,
                            onEdit = { onEditCategory(categoria) },
                            onDelete = { onDeleteRequest(categoria) },
                            canDelete = true
                        )
                    }

                    item {
                        AddItemCard(
                            label = "Agregar una categoría de $emptyText",
                            onClick = onNewCategory
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }

                Button(
                    onClick = onNextClick,
                    modifier = Modifier.weight(1f),
                    enabled = uiState.categoriasGasto.isNotEmpty() && uiState.categoriasIngreso.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Siguiente",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        FormBottomSheet(
            visible = uiState.showDialog,
            title = buildString {
                append(if (uiState.editingCategoria != null) "Editar " else "Nueva ")
                append("Categoría de ")
                append(if (uiState.tipoSeleccionado == TipoCategoria.GASTO) "Gasto" else "Ingreso")
            },
            onDismiss = onDismissForm
        ) {
            CategoryFormContent(
                state = CategoryFormState(
                    nombre = uiState.nombreCategoria,
                    nombreError = uiState.nombreError,
                    color = uiState.colorSeleccionado,
                    icono = uiState.iconoSeleccionado,
                    iconoError = uiState.iconoError,
                    formError = uiState.formError
                ),
                icons = icons,
                onNameChange = onNombreChange,
                onColorSelect = onColorSelect,
                onIconSelect = onIconSelect
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onDismissForm,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Cancelar", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSaveCategoria,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (uiState.isSaving) "Guardando..." else "Guardar",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        uiState.showDeleteConfirm?.let { categoria ->
            AlertDialog(
                onDismissRequest = onDismissDelete,
                title = {
                    Text(
                        text = "Eliminar categoría",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = "¿Estás seguro de que deseas eliminar \"${categoria.nombre}\"?",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                confirmButton = {
                    TextButton(onClick = onConfirmDelete) {
                        Text(text = "Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDelete) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
private fun EmptyCategoriesState(
    tipo: String,
    onNewCategory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val primaryContainer = MaterialTheme.colorScheme.primaryContainer
        val primary = MaterialTheme.colorScheme.primary

        Canvas(modifier = Modifier.size(120.dp)) {
            drawCircle(
                color = primaryContainer,
                radius = 60.dp.toPx()
            )
            drawLine(
                color = primary.copy(alpha = 0.3f),
                start = Offset(size.width * 0.3f, size.height * 0.5f),
                end = Offset(size.width * 0.7f, size.height * 0.5f),
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = primary.copy(alpha = 0.2f),
                start = Offset(size.width * 0.5f, size.height * 0.3f),
                end = Offset(size.width * 0.5f, size.height * 0.7f),
                strokeWidth = 2.dp.toPx()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Aún no tienes categorías de $tipo",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onNewCategory,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Crear primera categoría",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
