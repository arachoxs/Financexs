# Sistema de Diseño — FinanceXS

Este documento define la identidad visual, las reglas de experiencia de usuario (UX) y los tokens de diseño para **FinanceXS**. La aplicación adopta un enfoque **Dark-First Neo-Fintech Minimalista con Geometría Bauhaus**, priorizando la asimetría controlada, la legibilidad numérica y una estética tecnológica de alto nivel.

---

## 1. Filosofía de Diseño y Reglas UX

1. **Reducción de la Fatiga Visual (Anti-Halación):** No se utiliza texto blanco puro (`#FFFFFF`) sobre negro absoluto (`#000000`). Los contrastes se suavizan utilizando blancos platino y superficies grafito para proteger la vista del usuario en sesiones de uso prolongadas o nocturnas.

2. **Arquitectura de Capas (Elevation Layering):** La interfaz simula profundidad tridimensional mediante el brillo de los fondos. Los elementos más lejanos son más oscuros; los elementos más cercanos o clickeables son más claros.

3. **Uso Restringido del Color Semántico:** El color de identidad (Morado) guía la navegación. Los colores de estado (Verde/Rojo) se reservan **exclusivamente** para denotar el flujo del dinero (Ingresos/Gastos) y alertas críticas. Las categorías (comida, transporte, etc.) deben usar iconos monocromáticos para evitar la saturación cognitiva.

4. **Accesibilidad Numérica:** Los montos y balances se alinean a la derecha en listas para permitir comparación visual rápida. Se utiliza `fontFeatureSettings = "tnum"` (tabular nums) para que los dígitos se alineen verticalmente.

5. **Lienzo Geométrico Abstracto (Arte Bauhaus):** Los fondos de pantallas clave (Login/Welcome) y las tarjetas de datos principales (*Cards* de cuentas o balances) deben contener composiciones asimétricas de figuras geométricas sólidas (círculos, semicírculos y triángulos rectángulos) cruzadas por líneas finas vectoriales para generar identidad visual disruptiva.

---

## 2. Paleta de Colores (Tokens de Color)

### A. Modo Oscuro (Predeterminado)

| Token | Valor | Uso |
|-------|-------|-----|
| `color_background` | `#0B0814` | Fondo base. Negro profundo con matiz violeta, cohesión con marca |
| `color_surface` | `#16122B` | Tarjetas principales (items de lista, contenedores de cuentas) |
| `color_surface_variant` | `#231D3A` | Elementos emergentes, diálogos, Bottom Sheets, TextFields |
| `color_primary` | `#8B5CF6` | Violeta terroso vibrante. Estados seleccionados, balance total. WCAG AA (~6.2:1) |
| `color_primary_container` | `#1E1538` | Morado profundo de baja intensidad. Fondos sutiles para iconos/etiquetas |
| `color_on_primary` | `#FFFFFF` | Texto/iconos sobre `color_primary` |
| `color_on_surface` | `#F0ECF9` | Blanco lavanda. Títulos principales, nombres, montos |
| `color_on_surface_variant` | `#9B8FB8` | Gris violeta. Subtítulos, fechas, timestamps |
| `color_income` | `#34D399` | Verde eléctrico vibrante. Entradas de dinero, saldos positivos, signo `+` |
| `color_expense` | `#FB7185` | Coral intenso con presencia. Salidas de dinero, presupuestos excedidos, signo `-` |
| `color_error` | `#EF4444` | Rojo sofisticado. Transacciones fallidas, errores de validación |

### B. Modo Claro

| Token | Valor | Uso |
|-------|-------|-----|
| `color_background` | `#FAF8FF` | Fondo base. Blanco con tinte lavanda sutil, reduce brillo vs blanco puro |
| `color_surface` | `#FFFBFE` | Tarjetas. Blanco con tinte cálido, "flotan" sobre fondo |
| `color_surface_variant` | `#F3F0FA` | TextFields, buscadores, contenedores secundarios |
| `color_primary` | `#5B21B6` | Violeta profundo más rico. WCAG AA garantizado (~8.1:1) sobre fondo claro |
| `color_primary_container` | `#EDE9FE` | Lavanda vivo para fondos de iconos/etiquetas |
| `color_on_primary` | `#FFFFFF` | Texto/iconos sobre `color_primary` |
| `color_on_surface` | `#1A1530` | Marino profundo con matiz violeta (evita negro puro) |
| `color_on_surface_variant` | `#7C6F9B` | Gris violeta para fechas, subtítulos, metadatos |
| `color_income` | `#059669` | Verde vibrante. Optimizado para fondos blancos |
| `color_expense` | `#F43F5E` | Coral definido. Alerta de gasto con presencia visual |
| `color_error` | `#DC2626` | Rojo moderno para validación en modo claro |

---

## 3. Tipografía y Pesos Visuales

### Fuente Oficial: **Plus Jakarta Sans**

Sans-Serif geométrica con curvas amplias y modernas. Sus proporciones numéricas OpenType garantizan estabilidad visual en balances en tiempo real.

### Pesos y Jerarquía

| Estilo | Tamaño | Peso | Uso | `tnum` |
|--------|--------|------|-----|--------|
| `headlineLarge` | 32sp | Bold (700) | Balance total principal | Sí |
| `headlineMedium` | 28sp | Bold (700) | Montos destacados | Sí |
| `titleLarge` | 20sp | SemiBold (600) | Títulos de sección | No |
| `titleMedium` | 16sp | SemiBold (600) | Títulos de componentes, transacciones | No |
| `bodyLarge` | 16sp | Regular (400) | Texto principal de cuerpo | No |
| `bodyMedium` | 14sp | Regular (400) | Texto secundario de cuerpo | No |
| `labelLarge` | 14sp | Medium (500) | Botones, labels de acción | No |
| `labelMedium` | 12sp | Medium (500) | Chips, badges, metadata | No |
| `labelSmall` | 11sp | Medium (500) | Texto auxiliar mínimo | No |

**Nota:** No se usa ExtraBold (800) para evitar "glowing" en pantallas AMOLED en dark mode.

---

## 4. Componentes y Estilo Geométrico

### Radio de Esquinas (Corner Radius)

| Componente | Radio |
|------------|-------|
| Tarjetas de transacciones, contenedores de cuentas, gráficos | `24.dp` |
| Botones principales / Contenedores secundarios | `16.dp` |
| Campos de texto (TextFields) | `16.dp` |
| Chips / Badges | `Pill shape` (completo) |
| Botones de acción circular (`Top up`, etc.) | Totalmente esféricos (`CircleShape`) |

### Líneas Divisoras

**Prohibidas.** La separación se realiza mediante:
- Espaciado consistente (`8.dp` o `16.dp` de padding)
- Contraste nativo entre `color_background` y `color_surface`

### Iconografía

Se utilizan los iconos `Icons.Outlined` de Material 3. Las categorías usan iconos monocromáticos en `color_on_surface_variant` encerrados en contenedores esféricos oscuros sin bordes.

### Composición de Gráficos Analíticos

Los gráficos de líneas de tendencias financieras deben renderizarse de forma limpia: sin ejes de coordenadas cartesianas ($X/Y$), sin líneas de cuadrícula de fondo y con el nodo de valor máximo destacado mediante un indicador esférico brillante con una etiqueta flotante sólida.

### Cards de Cuentas y Transacciones

- **Efecto Visual:** Deben actuar como lienzos de recorte. El fondo de las tarjetas de las cuentas ejecuta una sub-composición de vectores geométricos asimétricos fijos (por ejemplo, la mitad de un círculo que intersecta la base inferior).
- **Interacción:** El cambio entre cuentas se realiza mediante un carrusel horizontal (*HorizontalPager*) donde la tarjeta secundaria asoma sutilmente en el borde de la pantalla.

---

## 5. Estados de UI

### Empty States

Cuando no hay datos (primera vez, o lista vacía):
- Ilustración geométrica abstracta constructivista en tonos `color_primary_container`.
- Texto descriptivo en `color_on_surface_variant`.
- Botón CTA (Call to Action) con `color_primary`.

### Loading States

Se utilizan **skeleton screens** (no spinners):
- Placeholders animados con gradiente sobre `color_surface`.
- Forma de las cards que se están cargando.
- Animación de shimmer sutil.

---

## 6. Guía de Implementación en Código

### Colores Semánticos vía CompositionLocal

```kotlin
data class FinanceColors(
    val income: Color,
    val expense: Color,
    val error: Color
)

val LocalFinanceColors = staticCompositionLocalOf<FinanceColors> {
    error("No FinanceColors provided")
}

// En Theme.kt
CompositionLocalProvider(
    LocalFinanceColors provides FinanceColors(
        income = if (darkTheme) Color(0xFF26E6A4) else Color(0xFF00BA88),
        expense = if (darkTheme) Color(0xFFFF6B8B) else Color(0xFFFF647C),
        error = if (darkTheme) Color(0xFFFF5252) else Color(0xFFD32F2F)
    )
) {
    MaterialTheme(colorScheme = colorScheme, ...) { content() }
}

// Uso en composables
val incomeColor = LocalFinanceColors.current.income
```

### Formato de Montos

```kotlin
@Composable
fun AmountText(
    amount: BigDecimal,
    isIncome: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isIncome) LocalFinanceColors.current.income
                else LocalFinanceColors.current.expense
    val prefix = if (isIncome) "+" else "-"

    Text(
        text = "$prefix${formatCurrency(amount)}",
        style = MaterialTheme.typography.headlineMedium.copy(
            fontFeatureSettings = "tnum"
        ),
        color = color,
        textAlign = TextAlign.End,
        modifier = modifier
    )
}
```
