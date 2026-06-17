package com.example.financexs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class IconOption(
    val name: String,
    val icon: ImageVector
)

val iconosCuentas = listOf(
    IconOption("AccountBalance", Icons.Outlined.AccountBalance),
    IconOption("Wallet", Icons.Outlined.Wallet),
    IconOption("CreditCard", Icons.Outlined.CreditCard),
    IconOption("Savings", Icons.Outlined.Savings),
    IconOption("Payments", Icons.Outlined.Payments),
    IconOption("Money", Icons.Outlined.Money),
    IconOption("Receipt", Icons.Outlined.Receipt),
    IconOption("Store", Icons.Outlined.Store),
    IconOption("ShoppingCart", Icons.Outlined.ShoppingCart),
    IconOption("Work", Icons.Outlined.Work),
    IconOption("TrendingUp", Icons.Outlined.TrendingUp),
    IconOption("AccountBalanceWallet", Icons.Outlined.AccountBalanceWallet)
)

@Composable
fun IconPicker(
    selectedIcon: String,
    onIconSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(iconosCuentas) { option ->
            val isSelected = option.name == selectedIcon

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .then(
                        if (isSelected) {
                            Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        } else {
                            Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        }
                    )
                    .clickable { onIconSelect(option.name) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.name,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(22.dp)
                )
            }
        }
    }
}
