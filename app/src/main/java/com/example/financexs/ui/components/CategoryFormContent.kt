package com.example.financexs.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class CategoryFormState(
    val nombre: String = "",
    val color: String = coloresDisponibles.first(),
    val icono: String = "",
    val nombreError: String? = null
)

@Composable
fun CategoryFormContent(
    state: CategoryFormState,
    icons: List<IconOption>,
    onNameChange: (String) -> Unit,
    onColorSelect: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        FormTextField(
            value = state.nombre,
            onValueChange = onNameChange,
            placeholder = "Ej: Alimentación",
            error = state.nombreError
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Color",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        ColorPicker(
            selectedColor = state.color,
            onColorSelect = onColorSelect
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Icono",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        IconPicker(
            icons = icons,
            selectedIcon = state.icono,
            onIconSelect = onIconSelect
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
