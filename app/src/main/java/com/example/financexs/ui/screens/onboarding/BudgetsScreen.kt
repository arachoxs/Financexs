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
import com.example.financexs.data.local.enums.PeriodoPresupuesto
import com.example.financexs.domain.model.Categoria
import com.example.financexs.domain.model.Presupuesto
import com.example.financexs.ui.components.AddItemCard
import com.example.financexs.ui.components.BudgetFormContent
import com.example.financexs.ui.components.BudgetFormState
import com.example.financexs.ui.components.FormBottomSheet
import com.example.financexs.ui.components.ItemCard
import com.example.financexs.ui.components.formatAmountWithSeparators

@Composable
fun BudgetsScreen(
    uiState: BudgetsUiState,
    onNewBudget: () -> Unit,
    onEditBudget: (Presupuesto) -> Unit,
    onDeleteRequest: (Presupuesto) -> Unit,
    onDismissForm: () -> Unit,
    onCategoriaSelect: (Long) -> Unit,
    onLimiteChange: (String) -> Unit,
    onPeriodoSelect: (PeriodoPresupuesto) -> Unit,
    onSavePresupuesto: () -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
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
                text = "Presupuestos",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Configura límites de gasto por categoría",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${uiState.presupuestos.size} presupuesto${if (uiState.presupuestos.size != 1) "s" else ""} configurado${if (uiState.presupuestos.size != 1) "s" else ""}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (uiState.presupuestos.isEmpty()) {
                Box(modifier = Modifier.weight(1f)) {
                    EmptyBudgetsState(onNewBudget = onNewBudget)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(uiState.presupuestos) { index, presupuesto ->
                        val categoria = uiState.categoriasGasto.find { it.id == presupuesto.categoriaId }
                        val periodoLabel = when (presupuesto.periodo) {
                            PeriodoPresupuesto.DIARIO -> "Diario"
                            PeriodoPresupuesto.SEMANAL -> "Semanal"
                            PeriodoPresupuesto.MENSUAL -> "Mensual"
                        }
                        val icon = com.example.financexs.ui.components.iconosCategoriasGasto
                            .find { it.name == categoria?.icono }?.icon
                            ?: com.example.financexs.ui.components.iconosCategoriasGasto.first().icon

                        ItemCard(
                            nombre = categoria?.nombre ?: "Sin categoría",
                            colorHex = categoria?.color ?: "#8B5CF6",
                            icon = icon,
                            subtitulo = "$ ${formatAmountWithSeparators(presupuesto.limite.toPlainString())} / $periodoLabel",
                            onEdit = { onEditBudget(presupuesto) },
                            onDelete = { onDeleteRequest(presupuesto) },
                            canDelete = true
                        )
                    }

                    item {
                        AddItemCard(
                            label = "Agregar un presupuesto",
                            onClick = onNewBudget
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

                if (uiState.presupuestos.isEmpty()) {
                    OutlinedButton(
                        onClick = onNextClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Omitir",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                } else {
                    Button(
                        onClick = onNextClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Finalizar",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        FormBottomSheet(
            visible = uiState.showDialog,
            title = if (uiState.editingPresupuesto != null) "Editar Presupuesto" else "Nuevo Presupuesto",
            onDismiss = onDismissForm
        ) {
            BudgetFormContent(
                state = BudgetFormState(
                    categoriaId = uiState.categoriaId,
                    limite = uiState.limite,
                    periodo = uiState.periodo,
                    categoriaError = uiState.categoriaError,
                    limiteError = uiState.limiteError,
                    formError = uiState.formError
                ),
                categorias = uiState.categoriasGasto,
                onCategoriaSelect = onCategoriaSelect,
                onLimiteChange = onLimiteChange,
                onPeriodoSelect = onPeriodoSelect
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
                onClick = onSavePresupuesto,
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

        uiState.showDeleteConfirm?.let { presupuesto ->
            AlertDialog(
                onDismissRequest = onDismissDelete,
                title = {
                    Text(
                        text = "Eliminar presupuesto",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = "¿Estás seguro de que deseas eliminar este presupuesto?",
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
private fun EmptyBudgetsState(onNewBudget: () -> Unit) {
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
            text = "Aún no tienes presupuestos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Es opcional, pero te ayuda a controlar tus gastos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNewBudget,
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
                text = "Crear primer presupuesto",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
