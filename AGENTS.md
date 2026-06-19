# AGENTS.md — Financexs

## Project state

Early-stage Android finance app (personal finance, 100% local storage). Package: `com.example.financexs`. Currently the **onboarding flow** is implemented (welcome, profile, accounts, categories). Home screen and CRUD not started.

## Docs

| Doc | Path | Notes |
|-----|------|-------|
| Requirements | `docs/REQUIREMENTS.md` | Business rules, entities, implementation order |
| Design System | `docs/DESIGN_SYSTEM.md` | Tokens, typography, components, UX rules |

## Build & run

```bash
./gradlew assembleDebug          # build debug APK
./gradlew test                   # unit tests (only ExampleUnitTest exists)
./gradlew connectedAndroidTest   # instrumented tests (needs device/emulator)
```

No CI workflows, lint, or typecheck scripts exist beyond Gradle defaults.

## `android` CLI (installed)

```bash
# Run app on device/emulator
android run
android run --debug

# UI inspection (faster than screenshots for debugging)
android layout              # full UI tree as JSON
android layout --diff       # only changes since last call

# Screenshots
android screen capture -o screen.png
android screen capture --annotate -o screen.png  # labeled screenshot

# Emulators
android emulator list
android emulator start <name>

# Official Android docs lookup
android docs search "Jetpack Compose basics"
android docs fetch kb://android/develop/ui/compose/tutorial

# SDK management
android sdk list --all
android sdk install <package>@<version>

# Skills
android skills list
android skills find <keyword>
```

**UI inspection workflow**: when the user asks for a UI change, run `android layout` first to read the current component tree before modifying code. Requires a device/emulator with the app open on the target screen.

## Naming convention — Spanish throughout

All entity, DAO, model, and repository names are in **Spanish**. Do not introduce English names.

| Layer | Examples |
|-------|----------|
| Entity | `PerfilEntity`, `CuentaEntity`, `CategoriaEntity`, `MovimientoEntity`, `PresupuestoEntity` |
| DAO | `PerfilDao`, `CuentaDao`, `CategoriaDao`, `MovimientoDao`, `PresupuestoDao` |
| Model | `Perfil`, `Cuenta`, `Categoria`, `Moneda` |
| Repository | `PerfilRepository`, `CuentaRepository`, `CategoriaRepository` |

No `Loan` entity exists yet — it's listed as a future feature in REQUIREMENTS.md §9.1.

## Architecture: MVVM + Repository (manual DI)

### Data flow

```
Composable → ViewModel → Repository → DAO → Room Database
```

UI never calls Repository directly.

### Entity ↔ Model mapping

Each entity has a `*Mapper.kt` file in `data/local/entity/` with `toDomain()` / `toEntity()` extension functions. Models are plain Kotlin data classes in `domain/model/`.

### Dependency injection — `AppModule` object

No Hilt. Dependencies wired in `di/AppModule.kt`:

```kotlin
object AppModule {
    fun init(context: Context)  // called once from FinanceXSApp.onCreate()

    val perfilRepository: PerfilRepository by lazy { ... }
    val cuentaRepository: CuentaRepository by lazy { ... }
    val categoriaRepository: CategoriaRepository by lazy { ... }
}
```

Always call `AppModule.init(context)` before accessing any dependency. ViewModels receive repositories via `ViewModelProvider.Factory` (see `OnboardingViewModelFactory` pattern).

### Navigation — Navigation3

Uses `NavKey` + `@Serializable` data objects for routes, NOT string-based NavGraph. Routes are defined in `ui/navigation/OnboardingNavGraph.kt`. Navigation uses `NavDisplay` with `entryProvider`.

## Current implementation status

| Feature | Status |
|---------|--------|
| Room DB (5 entities, v1) | Done |
| Onboarding: Welcome screen | Done |
| Onboarding: Profile screen | Done |
| Onboarding: Accounts, Categories, Budgets screens | Placeholder stubs |
| Home screen | Not started |
| Movements CRUD | Not started |
| Budget logic | Not started |

## Key tech stack

| Property | Value |
|----------|-------|
| AGP | 9.2.1 (built-in Kotlin) |
| compileSdk | 36 (minorApiLevel 1) |
| minSdk | 26 |
| Java | 17 |
| Build script | Kotlin DSL (.kts) |
| Version catalog | `gradle/libs.versions.toml` |
| Config cache | enabled |
| UI | Compose + Material3 + BOM 2026.05.01 |
| DB | Room 2.8.4 + KSP 2.3.9 |
| Nav | Navigation3 1.1.2 |
| Currency | `BigDecimal` (financial precision) |
| Dates | `java.time` |

## Conventions

- All dependency versions in `gradle/libs.versions.toml` — never hardcode in `build.gradle.kts`
- Use `./gradlew`, never bare `gradle`
- Kotlin DSL only (`.kts` files)
- No Dynamic Color — use `LocalFinanceColors.current` for semantic colors
- Amount formatting: right-aligned + `fontFeatureSettings = "tnum"` + semantic color
- Icons: `Icons.Outlined` from Material 3 (no external icon libraries)

## Error handling standard

### Two types of errors

| Type | Example | Display location |
|------|---------|------------------|
| **Validation** | "El nombre debe tener al menos 2 caracteres" | `supportingText` under its field |
| **Operation** | "Error al guardar. Intenta de nuevo." | `formError` banner at the top of the form |

### UiState fields

```kotlin
data class XxxUiState(
    // Form fields
    val nombre: String = "",
    val icono: String = "",

    // Validation errors — ONE PER FIELD (sufijo `Error`)
    val nombreError: String? = null,
    val iconoError: String? = null,

    // Operation error — ONE GENERAL (not field-specific)
    val formError: String? = null,

    val isSaving: Boolean = false,
    val showDialog: Boolean = false
)
```

### Validation rules

- Validate ALL fields before showing errors — no early `return` inside validation
- Collect errors in local variables, then set all at once
- Each `update*` clears ONLY its own validation error
- `showNewCategoryForm()` / `showEditCategory()` clear ALL errors when opening the form
- `formError` is cleared at the start of `save()`

### Display rules

- `nombreError` → right-aligned next to the field label via `FormTextField(error = ...)`
- `iconoError` → right-aligned next to the "Icono" label in a `Row` with `weight(1f)`
- `formError` → shown as `Text(bodySmall, error)` at the TOP of the form (banner)
- NO errors floating between form and buttons

### Visual layout

```
┌───────────────────────────────────────────────────────────────┐
│  Error al guardar. Intenta de nuevo.                          │  ← formError
│                                                               │
│  Nombre    El nombre debe tener al menos 2 caracteres        │  ← nombreError
│  ┌───────────────────────────────────────────────────────────┐│
│  │ Alimentación                                              ││
│  └───────────────────────────────────────────────────────────┘│
│                                                               │
│  Color                                                        │
│  [■] [■] [■] [■] [■] [■]                                     │
│                                                               │
│  Icono    Selecciona un icono                                 │  ← iconoError
│  [🛒] [🚗] [🏠] [❤️]                                         │
│                                                               │
│  [Cancelar]  [Guardar]                                        │
└───────────────────────────────────────────────────────────────┘
```

### Error mapping in Screen

```kotlin
CategoryFormContent(
    state = CategoryFormState(
        nombre = uiState.nombreCategoria,
        nombreError = uiState.nombreError,
        icono = uiState.iconoSeleccionado,
        iconoError = uiState.iconoError,
        formError = uiState.formError
    ),
    ...
)
```
