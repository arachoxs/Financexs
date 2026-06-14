# AGENTS.md — Financexs

## Project state

Android project configured with Jetpack Compose + Room. Package: `com.example.financexs`.

## Documentation

| Document | Location | Description |
|----------|----------|-------------|
| Requirements | `docs/REQUIREMENTS.md` | Business requirements, entities, rules, and technical decisions |



## Tech stack

| Property | Value |
|----------|-------|
| AGP | 9.2.1 (built-in Kotlin) |
| compileSdk | 36 (minorApiLevel 1) |
| minSdk | 26 |
| targetSdk | 36 |
| Build script | Kotlin DSL (.kts) |
| Version catalog | `gradle/libs.versions.toml` |
| Config cache | enabled |
| Java | 17 |

**Decided stack**: Jetpack Compose (UI) + Room (local storage).

**Libraries added**: Compose BOM 2026.05.01, Compose compiler 2.4.0, Room 2.8.4, KSP 2.3.9, Coroutines 1.11.0, Navigation3 1.1.2, Adaptive 1.2.0.

## Architecture: MVVM + Repository

### Overview

The app follows the **MVVM (Model-View-ViewModel)** pattern with a **Repository layer** for data access. This provides clear separation of concerns and testability.

### Layer Responsibilities

```
┌─────────────────────────────────────────────────────┐
│                    UI Layer                          │
│  Composables (Screens) ← ViewModels ← State/Flow   │
├─────────────────────────────────────────────────────┤
│                 Domain Layer (optional)              │
│              Use Cases / Business Logic              │
├─────────────────────────────────────────────────────┤
│                  Data Layer                          │
│         Repositories ← DAOs ← Room Database         │
└─────────────────────────────────────────────────────┘
```

### Package Structure

```
com.example.financexs/
├── data/
│   ├── local/
│   │   ├── database/
│   │   │   ├── AppDatabase.kt          # @Database class
│   │   │   └── Converters.kt           # TypeConverters
│   │   ├── dao/
│   │   │   ├── AccountDao.kt
│   │   │   ├── TransactionDao.kt
│   │   │   ├── CategoryDao.kt
│   │   │   ├── BudgetDao.kt
│   │   │   ├── LoanDao.kt
│   │   │   └── ...
│   │   └── entity/
│   │       ├── AccountEntity.kt
│   │       ├── TransactionEntity.kt
│   │       ├── CategoryEntity.kt
│   │       ├── BudgetEntity.kt
│   │       ├── LoanEntity.kt
│   │       └── ...
│   └── repository/
│       ├── AccountRepository.kt
│       ├── TransactionRepository.kt
│       ├── CategoryRepository.kt
│       ├── BudgetRepository.kt
│       ├── LoanRepository.kt
│       └── ...
├── domain/
│   └── model/
│       ├── Account.kt
│       ├── Transaction.kt
│       ├── Category.kt
│       ├── Budget.kt
│       ├── Loan.kt
│       └── ...
├── ui/
│   ├── navigation/
│   │   └── NavGraph.kt                 # Navigation3 setup
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   └── HomeViewModel.kt
│   │   ├── transactions/
│   │   │   ├── TransactionsScreen.kt
│   │   │   └── TransactionsViewModel.kt
│   │   ├── categories/
│   │   │   ├── CategoriesScreen.kt
│   │   │   └── CategoriesViewModel.kt
│   │   ├── loans/
│   │   │   ├── LoansScreen.kt
│   │   │   └── LoansViewModel.kt
│   │   └── settings/
│   │       ├── SettingsScreen.kt
│   │       └── SettingsViewModel.kt
│   └── components/                     # Shared composables
│       ├── TransactionCard.kt
│       ├── CategoryChip.kt
│       └── ...
└── di/
    └── AppModule.kt                    # Manual DI container
```

### Data Flow Rules

1. **UI → ViewModel**: UI calls ViewModel methods (never directly calls Repository)
2. **ViewModel → Repository**: ViewModel calls Repository methods
3. **Repository → DAO**: Repository calls DAO methods
4. **DAO → Database**: DAO executes Room queries

### Entity vs Model

- **Entity** (`data/local/entity/`): Room @Entity classes with annotations, used for database operations
- **Model** (`domain/model/`): Plain Kotlin data classes used in UI layer, free of Room annotations

### Repository Pattern

Each entity has a corresponding Repository that:
- Abstracts the data source (Room)
- Provides suspend functions for async operations
- Returns Flow<List<T>> for reactive data observation
- Handles data mapping from Entity → Model

Example:
```kotlin
class AccountRepository(private val accountDao: AccountDao) {
    fun getAllAccounts(): Flow<List<Account>> = 
        accountDao.getAllAccounts().map { entities -> 
            entities.map { it.toDomain() } 
        }
    
    suspend fun insertAccount(account: Account) = 
        accountDao.insert(account.toEntity())
}
```

### ViewModel Pattern

Each screen has a corresponding ViewModel that:
- Holds UI state as StateFlow or MutableState
- Exposes events for user actions
- Calls Repository methods
- Handles business logic (validation, calculations)

Example:
```kotlin
class AccountsViewModel(private val repository: AccountRepository) : ViewModel() {
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()
    
    init {
        viewModelScope.launch {
            repository.getAllAccounts().collect { _accounts.value = it }
        }
    }
    
    fun addAccount(account: Account) {
        viewModelScope.launch { repository.insertAccount(account) }
    }
}
```

### Manual DI (No Hilt)

Since we're not using Hilt, dependencies are provided manually:

```kotlin
// di/AppModule.kt
object AppModule {
    private val database = AppDatabase.getInstance(context)
    
    // DAOs
    private val accountDao = database.accountDao()
    private val transactionDao = database.transactionDao()
    
    // Repositories
    val accountRepository = AccountRepository(accountDao)
    val transactionRepository = TransactionRepository(transactionDao)
    
    // ViewModels (using ViewModelProvider.Factory)
    fun provideAccountsViewModel(): AccountsViewModelFactory { ... }
}
```

## Build & run

```bash
# Build debug APK
./gradlew assembleDebug

# Install + run on connected device/emulator
android run

# Run with debug flag
android run --debug

# List connected devices
adb devices
```

## Documentation workflow (android CLI)

This is the primary way to get official Android documentation during development:

```bash
# Step 1: Search for a topic
android docs search "Jetpack Compose basics"
android docs search "Room database kotlin"
android docs search "Hilt dependency injection"
android docs search "ViewModel lifecycle"
android docs search "Kotlin coroutines flow"

# Step 2: Fetch full article content (includes Kotlin code examples)
android docs fetch kb://android/develop/ui/compose/tutorial
android docs fetch kb://android/training/data-storage/room/index
android docs fetch kb://android/training/dependency-injection/hilt-android
```

**When to use**: Before implementing any Jetpack library feature. Search first, fetch the article, then implement following the official patterns.

## UI inspection

```bash
# Get full UI tree as JSON
android layout

# Get only changes since last call
android layout --diff

# Screenshot
android screen capture -o screen.png

# Annotated screenshot (labels on UI elements)
android screen capture --annotate -o screen.png

# Resolve annotated label to coordinates
android screen resolve --screen screen.png --string "#3"
```

## Skills (installed)

| Skill | Use case |
|-------|----------|
| `adaptive` | UI adaptativa (phones, tablets, foldables) |
| `edge-to-edge` | Edge-to-edge support |
| `navigation-3` | Navigation3 setup with Compose |
| `testing-setup` | Testing strategy (unit, UI, screenshot, e2e) |
| `jetpack-compose-m3` | Wear OS Compose Material3 |
| `verified-email` | Email verification with Credential Manager |
| `perfetto-trace-analysis` | Performance trace analysis |
| `r8-analyzer` | R8/ProGuard optimization |
| `android-cli` | CLI tool reference |

## Key commands reference

```bash
# SDK management
android sdk list --all
android sdk install <package>@<version>

# Emulator
android emulator list
android emulator start <name>

# Skills
android skills list
android skills find <keyword>
android skills add <skill>
android skills remove <skill>

# Version lookup (check latest library versions)
android studio version-lookup
```

## Conventions

- All dependencies go through `gradle/libs.versions.toml` — never hardcode versions in `build.gradle.kts`
- Use `./gradlew` wrapper, never bare `gradle`
- Kotlin DSL only (`.kts` files)
- Follow android-developer agent patterns in `.opencode/agents/`
