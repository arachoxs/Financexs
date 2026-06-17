package com.example.financexs.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class AccountFormState(
    val nombre: String = "",
    val nombreError: String? = null,
    val colorSeleccionado: String = "#8B5CF6",
    val iconoSeleccionado: String = "AccountBalance",
    val saldoInicial: String = "",
    val moneda: String = "COP"
)

@Composable
fun AccountFormContent(
    state: AccountFormState,
    onNombreChange: (String) -> Unit,
    onColorSelect: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onSaldoChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Nombre",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.nombre,
            onValueChange = onNombreChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Ej: Efectivo, Banco, Tarjeta",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            isError = state.nombreError != null,
            supportingText = if (state.nombreError != null) {
                { Text(text = state.nombreError, color = MaterialTheme.colorScheme.error) }
            } else null,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Color",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        ColorPicker(
            selectedColor = state.colorSeleccionado,
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
            selectedIcon = state.iconoSeleccionado,
            onIconSelect = onIconSelect
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Saldo inicial",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${formatAmountWithSeparators(state.saldoInicial.ifEmpty { "0" })} ${state.moneda}",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFeatureSettings = "tnum"
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(8.dp))

        AmountKeyboard(
            amount = state.saldoInicial,
            onAmountChange = onSaldoChange
        )
    }
}
