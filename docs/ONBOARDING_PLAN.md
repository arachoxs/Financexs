# Plan de Implementación — Configuración Inicial (Onboarding)

## Bloques

### Bloque 0 — Infraestructura Base
- PerfilRepository
- CuentaRepository
- CategoriaRepository
- di/AppModule.kt
- ui/navigation/OnboardingNavGraph.kt

### Bloque 1 — Pantalla de Bienvenida + Perfil
- WelcomeScreen.kt
- ProfileScreen.kt
- OnboardingViewModel.kt
- domain/model/Moneda.kt

### Bloque 2 — Creación de Cuentas
- AccountsScreen.kt
- ColorPicker.kt
- IconPicker.kt
- AmountKeyboard.kt

### Bloque 3 — Categorías de Gasto
- CategoriesExpenseScreen.kt
- CategoryFormSheet.kt

### Bloque 4 — Categorías de Ingreso
- CategoriesIncomeScreen.kt

### Bloque 5 — Presupuestos (Opcional)
- BudgetsScreen.kt
- BudgetFormSheet.kt

## Orden de ejecución
Bloque 0 → 1 → 2 → 3 → 4 → 5

## Estado actual
- [x] Bloque 0 completado
- [ ] Bloque 1 pendiente
- [ ] Bloque 2 pendiente
- [ ] Bloque 3 pendiente
- [ ] Bloque 4 pendiente
- [ ] Bloque 5 pendiente
