package com.example.financexs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financexs.ui.theme.FinancexsTheme
import com.example.financexs.ui.theme.LocalFinanceColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancexsTheme {
                TypographyShowcase()
            }
        }
    }
}

@Composable
fun TypographyShowcase() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // ── Brand header ─────────────────────────────
        Text(
            text = "FINANCEXS",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 4.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Balance Card ─────────────────────────────
        BalanceCard()

        Spacer(modifier = Modifier.height(24.dp))

        // ── Income / Expense ─────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IncomeExpenseCard(
                label = "Ingresos",
                amount = "$3,200,000",
                icon = Icons.Outlined.ArrowDownward,
                color = LocalFinanceColors.current.income,
                modifier = Modifier.weight(1f)
            )
            IncomeExpenseCard(
                label = "Gastos",
                amount = "$750,000",
                icon = Icons.Outlined.ArrowUpward,
                color = LocalFinanceColors.current.expense,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── Section title (Bricolage display) ────────
        Text(
            text = "Movimientos recientes",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ── Transactions ─────────────────────────────
        TransactionItem(
            icon = Icons.Outlined.Restaurant,
            title = "Almuerzo",
            subtitle = "Hoy, 12:30 PM",
            amount = "-$35,000",
            isIncome = false
        )

        Spacer(modifier = Modifier.height(10.dp))

        TransactionItem(
            icon = Icons.Outlined.Straighten,
            title = "Salario",
            subtitle = "Ayer, 9:00 AM",
            amount = "+$3,200,000",
            isIncome = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        TransactionItem(
            icon = Icons.Outlined.Coffee,
            title = "Café",
            subtitle = "Ayer, 7:15 AM",
            amount = "-$8,500",
            isIncome = false
        )

        Spacer(modifier = Modifier.height(10.dp))

        TransactionItem(
            icon = Icons.Outlined.Flight,
            title = "Vuelo Bogotá",
            subtitle = "12 Jun, 10:00 AM",
            amount = "-$420,000",
            isIncome = false
        )

        Spacer(modifier = Modifier.height(10.dp))

        TransactionItem(
            icon = Icons.Outlined.ShoppingBag,
            title = "Freelance diseño",
            subtitle = "10 Jun, 3:00 PM",
            amount = "+$850,000",
            isIncome = true
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ── Typography Showcase ──────────────────────
        Text(
            text = "Tipografía",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display example
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Bricolage Grotesque",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Display & Headlines",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ABCDEfghij 0123456789",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Body example
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "DM Sans",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Body & UI Text",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz 0123456789",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun BalanceCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            BauhausBackground(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Balance total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$2,450,000",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "COP",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BauhausBackground(modifier: Modifier = Modifier) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        drawCircle(
            color = primaryColor.copy(alpha = 0.08f),
            radius = 120.dp.toPx(),
            center = Offset(width * 0.85f, height * 0.1f)
        )

        drawCircle(
            color = primaryColor.copy(alpha = 0.12f),
            radius = 80.dp.toPx(),
            center = Offset(width * 0.15f, height * 0.85f)
        )

        drawCircle(
            color = primaryColor.copy(alpha = 0.2f),
            radius = 24.dp.toPx(),
            center = Offset(width * 0.7f, height * 0.65f)
        )

        val trianglePath = Path().apply {
            moveTo(width * 0.75f, height * 0.2f)
            lineTo(width * 0.95f, height * 0.7f)
            lineTo(width * 0.55f, height * 0.7f)
            close()
        }
        drawPath(
            path = trianglePath,
            color = primaryColor.copy(alpha = 0.06f)
        )

        drawLine(
            color = primaryColor.copy(alpha = 0.15f),
            start = Offset(0f, height * 0.45f),
            end = Offset(width * 0.4f, height * 0.45f),
            strokeWidth = 2.dp.toPx()
        )

        drawLine(
            color = primaryColor.copy(alpha = 0.1f),
            start = Offset(width * 0.6f, 0f),
            end = Offset(width, height * 0.35f),
            strokeWidth = 1.5.dp.toPx()
        )
    }
}

@Composable
private fun IncomeExpenseCard(
    label: String,
    amount: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = amount,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFeatureSettings = "tnum"
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TransactionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    amount: String,
    isIncome: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val amountColor = if (isIncome) LocalFinanceColors.current.income
            else LocalFinanceColors.current.expense

            Text(
                text = amount,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontFeatureSettings = "tnum"
                ),
                color = amountColor,
                textAlign = TextAlign.End
            )
        }
    }
}

// ── Previews ────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true, name = "Dark Mode")
@Composable
private fun TypographyShowcaseDark() {
    FinancexsTheme(darkTheme = true) {
        TypographyShowcase()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Light Mode")
@Composable
private fun TypographyShowcaseLight() {
    FinancexsTheme(darkTheme = false) {
        TypographyShowcase()
    }
}
