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
import com.example.financexs.ui.components.AccountFormContent
import com.example.financexs.ui.components.AccountFormState
import com.example.financexs.ui.components.AddItemCard
import com.example.financexs.ui.components.FormBottomSheet
import com.example.financexs.ui.components.ItemCard
import com.example.financexs.ui.components.formatAmountWithSeparators
import com.example.financexs.ui.components.iconosCuentas

@Composable
fun AccountsScreen(
    uiState: AccountsUiState,
    onNewAccount: () -> Unit,
    onEditAccount: (com.example.financexs.domain.model.Cuenta) -> Unit,
    onDeleteRequest: (com.example.financexs.domain.model.Cuenta) -> Unit,
    onDismissForm: () -> Unit,
    onNombreChange: (String) -> Unit,
    onColorSelect: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onSaldoChange: (String) -> Unit,
    onSaveCuenta: () -> Unit,
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
                text = "Tus Cuentas",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Crea al menos una cuenta para comenzar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${uiState.cuentas.size} cuenta${if (uiState.cuentas.size != 1) "s" else ""} creada${if (uiState.cuentas.size != 1) "s" else ""}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (uiState.cuentas.isEmpty()) {
                EmptyAccountsState(onNewAccount = onNewAccount)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(uiState.cuentas) { index, cuenta ->
                        ItemCard(
                            nombre = cuenta.nombre,
                            colorHex = cuenta.color,
                            icon = iconosCuentas.find { it.name == cuenta.icono }?.icon
                                ?: iconosCuentas.first().icon,
                            subtitulo = "$ ${formatAmountWithSeparators(cuenta.saldo.toPlainString())}",
                            onEdit = { onEditAccount(cuenta) },
                            onDelete = { onDeleteRequest(cuenta) },
                            canDelete = uiState.cuentas.size > 1
                        )
                    }

                    item {
                        AddItemCard(
                            label = "Agregar una cuenta nueva",
                            onClick = onNewAccount
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
                    enabled = uiState.cuentas.isNotEmpty(),
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
            title = if (uiState.editingCuenta != null) "Editar Cuenta" else "Nueva Cuenta",
            onDismiss = onDismissForm
        ) {
            uiState.formError?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            AccountFormContent(
                state = AccountFormState(
                    nombre = uiState.nombreCuenta,
                    nombreError = uiState.nombreError,
                    colorSeleccionado = uiState.colorSeleccionado,
                    iconoSeleccionado = uiState.iconoSeleccionado,
                    saldoInicial = uiState.saldoInicial,
                    moneda = uiState.moneda
                ),
                onNombreChange = onNombreChange,
                onColorSelect = onColorSelect,
                onIconSelect = onIconSelect,
                onSaldoChange = onSaldoChange
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
                onClick = onSaveCuenta,
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

        uiState.showDeleteConfirm?.let { cuenta ->
            AlertDialog(
                onDismissRequest = onDismissDelete,
                title = {
                    Text(
                        text = "Eliminar cuenta",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = "¿Estás seguro de que deseas eliminar \"${cuenta.nombre}\"?",
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
private fun EmptyAccountsState(onNewAccount: () -> Unit) {
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
            text = "Aún no tienes cuentas",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onNewAccount,
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
                text = "Crear primera cuenta",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
