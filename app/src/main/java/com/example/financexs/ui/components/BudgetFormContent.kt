package com.example.financexs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.financexs.data.local.enums.PeriodoPresupuesto
import com.example.financexs.domain.model.Categoria

data class BudgetFormState(
    val categoriaId: Long = 0,
    val limite: String = "",
    val periodo: PeriodoPresupuesto = PeriodoPresupuesto.MENSUAL,
    val categoriaError: String? = null,
    val limiteError: String? = null,
    val formError: String? = null
)

@Composable
fun BudgetFormContent(
    state: BudgetFormState,
    categorias: List<Categoria>,
    onCategoriaSelect: (Long) -> Unit,
    onLimiteChange: (String) -> Unit,
    onPeriodoSelect: (PeriodoPresupuesto) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Operation error — banner at top
        if (state.formError != null) {
            Text(
                text = state.formError.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Categoría dropdown
        CategoriaDropdown(
            categorias = categorias,
            selectedId = state.categoriaId,
            onSelect = onCategoriaSelect,
            error = state.categoriaError
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Límite
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Límite",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            if (state.limiteError != null) {
                Text(
                    text = state.limiteError.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${formatAmountWithSeparators(state.limite.ifEmpty { "0" })} COP",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFeatureSettings = "tnum"
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(8.dp))

        AmountKeyboard(
            amount = state.limite,
            onAmountChange = onLimiteChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Período
        Text(
            text = "Período",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        PeriodoSelector(
            selected = state.periodo,
            onSelect = onPeriodoSelect
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun CategoriaDropdown(
    categorias: List<Categoria>,
    selectedId: Long,
    onSelect: (Long) -> Unit,
    error: String?
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategoria = categorias.find { it.id == selectedId }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Categoría de gasto",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.weight(1f))

        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedCategoria != null) {
                Text(
                    text = selectedCategoria.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(
                    text = "Seleccionar categoría",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }

            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categorias.forEach { categoria ->
                DropdownMenuItem(
                    text = { Text(categoria.nombre) },
                    onClick = {
                        onSelect(categoria.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PeriodoSelector(
    selected: PeriodoPresupuesto,
    onSelect: (PeriodoPresupuesto) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PeriodoPresupuesto.entries.forEach { periodo ->
            val isSelected = periodo == selected
            val label = when (periodo) {
                PeriodoPresupuesto.DIARIO -> "Diario"
                PeriodoPresupuesto.SEMANAL -> "Semanal"
                PeriodoPresupuesto.MENSUAL -> "Mensual"
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (isSelected) {
                            Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                        } else {
                            Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                        }
                    )
                    .clickable { onSelect(periodo) }
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .then(
                            if (isSelected) {
                                Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            } else {
                                Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}
