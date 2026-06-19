package com.example.financexs.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financexs.data.local.enums.TipoCategoria
import com.example.financexs.ui.theme.LocalFinanceColors

@Composable
fun CategoryTypeToggle(
    selected: TipoCategoria,
    onSelect: (TipoCategoria) -> Unit,
    modifier: Modifier = Modifier
) {
    val financeColors = LocalFinanceColors.current
    val density = LocalDensity.current
    val isExpense = selected == TipoCategoria.GASTO

    var rowWidthPx by remember { mutableIntStateOf(0) }

    val animatedOffsetX by animateDpAsState(
        targetValue = with(density) {
            if (rowWidthPx > 0) {
                if (isExpense) 0.dp else (rowWidthPx / 2).toDp()
            } else {
                0.dp
            }
        },
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "indicatorOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        // Animated sliding indicator — hidden until measured
        Box(
            modifier = Modifier
                .offset { IntOffset(x = animatedOffsetX.roundToPx(), y = 0) }
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .drawWithContent {
                    if (rowWidthPx > 0) drawContent()
                }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    rowWidthPx = coordinates.size.width
                }
        ) {
            TabOption(
                label = "Gastos",
                isSelected = isExpense,
                accentColor = financeColors.expense,
                contentDescription = "Categorías de gasto",
                onClick = { onSelect(TipoCategoria.GASTO) },
                modifier = Modifier.weight(1f)
            )
            TabOption(
                label = "Ingresos",
                isSelected = !isExpense,
                accentColor = financeColors.income,
                contentDescription = "Categorías de ingreso",
                onClick = { onSelect(TipoCategoria.INGRESO) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TabOption(
    label: String,
    isSelected: Boolean,
    accentColor: Color,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .semantics {
                this.role = Role.Tab
                this.selected = isSelected
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) accentColor else Color.Transparent)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 14.sp
                ),
                color = if (isSelected) accentColor
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
