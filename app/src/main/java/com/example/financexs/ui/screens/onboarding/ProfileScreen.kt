package com.example.financexs.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financexs.data.local.enums.TemaApp
import com.example.financexs.domain.model.Moneda
import com.example.financexs.domain.model.monedasDisponibles

@Composable
fun ProfileScreen(
    uiState: OnboardingUiState,
    onNombreChange: (String) -> Unit,
    onMonedaSelect: (Moneda) -> Unit,
    onTemaSelect: (TemaApp) -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Tu Perfil",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Configura tu información básica",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Nombre
        Text(
            text = "Nombre",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = onNombreChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Tu nombre",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            isError = uiState.nombreError != null,
            supportingText = uiState.nombreError?.let { error ->
                { Text(text = error, color = MaterialTheme.colorScheme.error) }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Moneda
        Text(
            text = "Moneda principal",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        MonedaGrid(
            monedas = monedasDisponibles,
            selected = uiState.monedaSeleccionada,
            onSelect = onMonedaSelect
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Tema
        Text(
            text = "Tema visual",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        TemaSelector(
            selected = uiState.temaSeleccionado,
            onSelect = onTemaSelect
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Botón Siguiente
        Button(
            onClick = onNextClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = if (uiState.isSaving) "Guardando..." else "Siguiente",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun MonedaGrid(
    monedas: List<Moneda>,
    selected: Moneda,
    onSelect: (Moneda) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        monedas.chunked(2).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { moneda ->
                    MonedaCard(
                        moneda = moneda,
                        isSelected = moneda.codigo == selected.codigo,
                        onClick = { onSelect(moneda) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MonedaCard(
    moneda: Moneda,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = moneda.simbolo,
                style = MaterialTheme.typography.titleLarge,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = moneda.codigo,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = moneda.nombre,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TemaSelector(
    selected: TemaApp,
    onSelect: (TemaApp) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TemaCard(
            tema = TemaApp.SISTEMA,
            label = "Sistema",
            icon = Icons.Outlined.Settings,
            isSelected = selected == TemaApp.SISTEMA,
            onClick = { onSelect(TemaApp.SISTEMA) },
            modifier = Modifier.weight(1f)
        )
        TemaCard(
            tema = TemaApp.CLARO,
            label = "Claro",
            icon = Icons.Outlined.LightMode,
            isSelected = selected == TemaApp.CLARO,
            onClick = { onSelect(TemaApp.CLARO) },
            modifier = Modifier.weight(1f)
        )
        TemaCard(
            tema = TemaApp.OSCURO,
            label = "Oscuro",
            icon = Icons.Outlined.DarkMode,
            isSelected = selected == TemaApp.OSCURO,
            onClick = { onSelect(TemaApp.OSCURO) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TemaCard(
    tema: TemaApp,
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
