package com.example.financexs.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    icons: List<IconOption> = iconosCuentas,
    onNombreChange: (String) -> Unit,
    onColorSelect: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onSaldoChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        FormTextField(
            value = state.nombre,
            onValueChange = onNombreChange,
            placeholder = "Ej: Efectivo, Banco, Tarjeta",
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
            icons = icons,
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
