# Requerimientos del Sistema - Financexs

## 1. Resumen Ejecutivo

App móvil de gestión financiera personal con almacenamiento 100% local (Room). Permite manejar múltiples cuentas, registrar movimientos (ingresos/gastos/transferencias), gestionar categorías, configurar presupuestos por categoría y administrar un perfil personal con configuraciones de la app.

**Stack tecnológico**: Kotlin, Jetpack Compose, Material3, Room, Navigation3

---

## 2. Configuración Inicial

Cuando la app se abre por primera vez (sin datos guardados), se debe guiar al usuario a través de un flujo de configuración inicial personalizada. **No hay datos por defecto** - el usuario configura todo desde cero.

### 2.1 Flujo de Configuración Inicial

```
┌─────────────────────────────────────────────────────────────┐
│                    Pantalla de Bienvenida                    │
│              "Configura tu espacio financiero"               │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Paso 1: Perfil                            │
│    Configura tu nombre, moneda principal y tema visual       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Paso 2: Cuentas                           │
│      Crea al menos una cuenta (nombre, color, icono,        │
│      saldo)                                                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                Paso 3: Categorías de Gasto                   │
│      Crea las categorías para tus gastos                    │
│      (Alimentación, Transporte, Entretenimiento, etc.)      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│               Paso 4: Categorías de Ingreso                  │
│      Crea las categorías para tus ingresos                  │
│      (Salario, Freelance, Inversiones, etc.)                │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│              Paso 5: Presupuestos (Opcional)                 │
│      Configura límites de gasto por categoría               │
│      (Diario, Semanal o Mensual)                            │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   ¡Listo! Comenzar a usar la app             │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Detalle de Cada Paso

**Paso 1: Configuración de Perfil**
- El usuario ingresa su nombre
- Selecciona su moneda principal de una lista (COP, USD, EUR, MXN, etc.)
- Selecciona el tema visual: Sistema (predeterminado), Claro u Oscuro
- Se puede cambiar después en Configuración

**Paso 2: Creación de Cuentas**
- El usuario debe crear al menos una cuenta
- Campos obligatorios: nombre, color, icono, saldo
- El saldo puede ser 0 o cualquier monto (representa el balance actual al momento de crear la cuenta)
- Puede crear múltiples cuentas en este paso
- Ejemplos: "Efectivo", "Cuenta Bancaria", "Tarjeta de Crédito"

**Paso 3: Categorías de Gasto**
- El usuario crea las categorías de gasto que necesita
- No hay categorías predefinidas
- Campos: nombre, icono, color
- Ejemplos: "Alimentación", "Transporte", "Entretenimiento", "Servicios"

**Paso 4: Categorías de Ingreso**
- El usuario crea las categorías de ingreso que necesita
- No hay categorías predefinidas
- Campos: nombre, icono, color
- Ejemplos: "Salario", "Freelance", "Inversiones", "Regalos"

**Paso 5: Presupuestos (Opcional)**
- El usuario puede configurar límites de gasto por categoría
- Puede definir múltiples límites por categoría (diario, semanal, mensual)
- Este paso es opcional - puede omitirse y configurarse después

### 2.3 Reglas de Configuración Inicial

- **Obligatorios**: Perfil (nombre, moneda, tema) + al menos una cuenta
- **Opcionales**: Categorías y presupuestos (pero se recomienda configurar al menos las categorías básicas)
- **Sin datos por defecto**: Todo es personalizado por el usuario
- **Puede omitirse**: El usuario puede saltar pasos y configurar después
- **Edición posterior**: Todo lo configurado puede modificarse después

### 2.4 Detección de Primera Vez

- La app verifica si existe el perfil en la base de datos
- Si no existe → muestra el flujo de configuración inicial
- Si existe → muestra la pantalla principal
- Se puede acceder a la configuración desde el menú en cualquier momento

---

## 3. Entidades del Sistema

### 3.1 Cuenta

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| nombre | String | Sí | Nombre de la cuenta |
| color | String | Sí | Color en formato hex para identificación visual |
| icono | String | Sí | Nombre de Material Icon (ej: "Wallet", "CreditCard") |
| saldo | Decimal | Sí | Saldo actual de la cuenta (se setea al crear, se modifica con cada movimiento) |
| fechaCreacion | DateTime | Auto | Fecha de creación |

**Reglas de negocio:**
- Cantidad ilimitada de cuentas
- Saldo configurable al crear la cuenta (puede ser 0 o cualquier monto)
- Cada ingreso suma al saldo, cada gasto resta, cada transferencia resta en origen y suma en destino
- No se puede eliminar una cuenta con movimientos asociados

---

### 3.2 Movimiento

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| tipo | Enum | Sí | INGRESO, GASTO, TRANSFERENCIA |
| monto | Decimal | Sí | Monto del movimiento |
| categoriaId | Long | No | FK a categoría (obligatorio para INGRESO/GASTO, null para TRANSFERENCIA) |
| cuentaId | Long | Sí* | FK a cuenta (*obligatorio para INGRESO/GASTO, null para TRANSFERENCIA) |
| cuentaOrigenId | Long | No | FK a cuenta origen (solo para TRANSFERENCIA) |
| cuentaDestinoId | Long | No | FK a cuenta destino (solo para TRANSFERENCIA) |
| descripcion | String | No | Descripción opcional |
| fecha | Date | Sí | Fecha del movimiento (actual por defecto, configurable) |
| fechaRegistro | DateTime | Auto | Timestamp de cuándo se registró |

**Reglas de negocio:**
- CRUD completo sobre movimientos
- INGRESO: suma al saldo de `cuentaId`, requiere `categoriaId`
- GASTO: resta al saldo de `cuentaId`, requiere `categoriaId`
- TRANSFERENCIA: resta al saldo de `cuentaOrigenId` y suma al saldo de `cuentaDestinoId`, no requiere categoría
- Fecha configurable: se puede registrar en cualquier fecha pasada o futura
- Al registrar un gasto, verificar presupuesto de la categoría y mostrar indicador visual si se acerca o supera el límite

---

### 3.3 Categoría

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| nombre | String | Sí | Nombre de la categoría |
| tipo | Enum | Sí | INGRESO o GASTO |
| icono | String | Sí | Nombre de Material Icon |
| color | String | Sí | Color en formato hex |

**Reglas de negocio:**
- Categorías separadas por tipo: una categoría de gasto no puede usarse para ingresos y viceversa
- El usuario crea todas las categorías (no hay predefinidas)
- CRUD completo
- Iconos de Material Icons (mismo sistema que las cuentas)
- No se puede eliminar una categoría con movimientos asociados

---

### 3.4 Presupuesto

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| categoriaId | Long | Sí | FK a categoría |
| limite | Decimal | Sí | Monto límite |
| periodo | Enum | Sí | DIARIO, SEMANAL, MENSUAL |

**Reglas de negocio:**
- Una categoría puede tener múltiples presupuestos (uno diario, otro mensual, etc.) - todos opcionales
- Configurar presupuestos es opcional
- No bloquea el gasto, solo informa con indicador visual
- Indicadores: normal (lejos del límite), alerta (cerca del límite), excedido (superado)

**Cálculo del período (lazy):**
- No se almacena fecha de inicio del período — se calcula al consultar según la fecha actual del dispositivo
- **Mensual**: gastos desde el día 1 del mes actual
- **Semanal**: gastos desde el lunes de la semana actual
- **Diario**: gastos del día actual
- El "reset" es implícito: cuando la fecha cambia, el período se desplaza automáticamente
- No requiere rutina programada — se resuelve al momento de consultar el estado del presupuesto

---

### 3.5 Perfil

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Int | Fijo (1) | Siempre existe un solo perfil |
| nombre | String | Sí | Nombre del usuario |
| moneda | String | Sí | Moneda principal (COP, USD, EUR, etc.) |
| tema | String | Sí | SISTEMA, CLARO o OSCURO |

**Reglas de negocio:**
- Solo existe un perfil (id siempre es 1)
- Se crea durante la configuración inicial
- Se puede editar desde Configuración
- La moneda se usa para mostrar símbolos en movimientos y cuentas

---

## 4. Vistas Principales

### 4.1 Home / Registro de Movimientos

Pantalla principal de la app.

- Muestra resumen de saldo total
- Lista de movimientos agrupados por día
- Filtros por: cuenta, categoría, tipo, rango de fechas
- Acciones: crear nuevo ingreso, crear nuevo gasto, crear transferencia
- Cada movimiento muestra: monto, categoría, cuenta, fecha, descripción
- Indicador visual de presupuesto en movimientos de gasto

### 4.2 Gestión de Categorías

- Lista de categorías agrupadas por tipo (ingreso/gasto)
- CRUD completo
- Configuración de presupuesto por categoría
- Indicador visual de estado del presupuesto

---

## 5. Configuración

- **Perfil**: editable desde Configuración (nombre, moneda, tema)
- **Moneda**: configurable, se puede cambiar en cualquier momento
- **Tema**: Claro / Oscuro / Sistema (sigue preferencia del sistema)
- **Sin autenticación**: la app se abre directamente
- **Sin exportación/reportes**: funcionalidad futura
- **Sin recurrencia**: todos los movimientos son manuales

---

## 6. Decisiones Técnicas

| Decisión | Valor | Razón |
|----------|-------|-------|
| Navegación | Navigation3 | Ya declarado como dependencia, más moderno |
| DI | Manual (sin Hilt) | Proyecto simple, no requiere framework pesado |
| Arquitectura | MVVM + Repository | Estándar Android, ya soportado por dependencias |
| Persistencia | Room | Ya configurado con KSP |
| UI | Compose + Material3 | Ya configurado |
| Moneda | BigDecimal | Precisión financiera |
| Fechas | java.time | API moderna, soporte desde API 26 |

---

## 7. Orden de Implementación

1. **Configuración Inicial**: Flujo de setup + detección de primera vez
2. **Capa de datos**: Entities, DAOs, Database, TypeConverters
3. **Repositorios**: Implementaciones de acceso a datos
4. **ViewModels**: Lógica de negocio por pantalla
5. **Navegación**: NavDisplay con rutas
6. **Pantallas**: UI de cada vista principal
7. **Presupuestos**: Lógica de alertas e indicadores

---

## 8. Verificación

- Compilar sin errores: `./gradlew assembleDebug`
- Verificar configuración inicial: borrar datos de la app y verificar que muestra el flujo
- Verificar Room: tests de DAO con Room testing
- Verificar UI: navegación entre pantallas, CRUD de entidades
- Verificar presupuestos: registrar gasto y ver indicador visual

---

## 9. Ideas Futuras

Funcionalidades a implementar en futuras versiones:

### 9.1 Gestión de Préstamos

**Entidades:**

- **Préstamo Otorgado**: Persona, monto, fecha límite, estado, pagos parciales
- **Pago de Préstamo Otorgado**: Monto pagado, fecha, descripción
- **Préstamo Recibido / Compra a Cuotas**: Título, monto, plazo en meses, día de pago, interés
- **Pago de Préstamo Recibido**: Monto pagado, fecha, descripción

**Reglas:**
- Los movimientos de préstamos no se mezclan con gastos/ingresos
- Usar tipos de movimiento separados: `PRESTAMO_OTORGADO`, `PRESTAMO_OTORGADO_COBRO`, `PRESTAMO_RECIBIDO`, `PRESTAMO_RECIBIDO_PAGO`
- Los tipos de préstamo no requieren categoría (categoriaId opcional)
- Tienen su propia sección en reportes: "Préstamos por cobrar" / "Deudas pendientes"

### 9.2 Exportación/Reportes

- Exportar movimientos a CSV
- Reportes por categoría, por cuenta, por período
- Gráficas de tendencias

### 9.3 Movimientos Recurrentes

- Configurar movimientos que se repiten automáticamente (alquiler, servicios, etc.)
- Configurar frecuencia: diario, semanal, mensual, anual
