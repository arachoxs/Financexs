# Requerimientos del Sistema - Financexs

## 1. Resumen Ejecutivo

App móvil de gestión financiera personal con almacenamiento 100% local (Room). Permite manejar múltiples cuentas, registrar movimientos (ingresos/gastos/transferencias), gestionar categorías, configurar presupuestos por categoría, y administrar préstamos (otorgados y recibidos).

**Stack tecnológico**: Kotlin, Jetpack Compose, Material3, Room, Navigation3

---

## 2. Entidades del Sistema

### 2.1 Cuenta

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| nombre | String | Sí | Nombre de la cuenta |
| tipo | String | Sí | Tipo personalizado por el usuario (ej: "Efectivo", "Bancaria", "Tarjeta") |
| color | String | Sí | Color en formato hex para identificación visual |
| icono | String | Sí | Nombre de Material Icon (ej: "Wallet", "CreditCard") |
| saldoInicial | Decimal | Sí | Saldo definido por el usuario al crear la cuenta |
| saldo | Decimal | Auto | Calculado: saldoInicial + sum(movimientos) |
| fechaCreacion | DateTime | Auto | Fecha de creación |

**Reglas de negocio:**
- Cantidad ilimitada de cuentas
- Saldo inicial configurable al crear la cuenta
- Transferencia entre cuentas propias genera dos movimientos vinculados (salida en origen, entrada en destino)
- No se puede eliminar una cuenta con movimientos asociados

---

### 2.2 Movimiento

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| tipo | Enum | Sí | INGRESO, GASTO, TRANSFERENCIA_ENTRADA, TRANSFERENCIA_SALIDA |
| monto | Decimal | Sí | Monto del movimiento |
| categoriaId | Long | Sí | FK a categoría |
| cuentaId | Long | Sí | FK a cuenta |
| descripcion | String | No | Descripción opcional |
| fecha | Date | Sí | Fecha del movimiento (actual por defecto, configurable) |
| fechaRegistro | DateTime | Auto | Timestamp de cuándo se registró |
| transferenciaId | Long | No | ID que vincula los dos movimientos de una transferencia |

**Reglas de negocio:**
- CRUD completo sobre movimientos
- Transferencias entre cuentas se registran como dos movimientos vinculados (salida en cuenta origen + entrada en cuenta destino)
- Fecha configurable: se puede registrar en cualquier fecha pasada o futura
- Al registrar un gasto, verificar presupuesto de la categoría y mostrar indicador visual si se acerca o supera el límite

---

### 2.3 Categoría

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

### 2.4 Presupuesto

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| categoriaId | Long | Sí | FK a categoría |
| limite | Decimal | Sí | Monto límite |
| periodo | Enum | Sí | DIARIO, SEMANAL, MENSUAL |
| fechaInicio | Date | Auto | Inicio del período actual |

**Reglas de negocio:**
- Una categoría puede tener múltiples presupuestos (uno diario, otro mensual, etc.) - todos opcionales
- Configurar presupuestos es opcional
- Se resetea al pasar el período (no se acumula)
- No bloquea el gasto, solo informa con indicador visual
- Indicadores: normal (lejos del límite), alerta (cerca del límite), excedido (superado)

---

### 2.5 Préstamo Otorgado (a otra persona)

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| personaNombre | String | Sí | Nombre de la persona |
| monto | Decimal | Sí | Monto prestado |
| descripcion | String | No | Descripción opcional |
| fechaLimite | Date | No | Fecha límite de pago opcional |
| estado | Enum | Auto | ACTIVO, PAGADO_PARCIAL, PAGADO_TOTAL |
| montoPagado | Decimal | Auto | Suma de pagos recibidos |

**Reglas de negocio:**
- Préstamos amistosos sin interés
- Se pueden registrar pagos parciales
- Estado se actualiza automáticamente según pagos

---

### 2.6 Pago de Préstamo Otorgado

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| prestamoId | Long | Sí | FK a préstamo |
| monto | Decimal | Sí | Monto del pago |
| fecha | Date | Sí | Fecha del pago |
| descripcion | String | No | Nota opcional |

---

### 2.7 Préstamo Recibido / Compra a Cuotas

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| titulo | String | Sí | Nombre del préstamo/compra |
| monto | Decimal | Sí | Monto total prestado |
| descripcion | String | No | Descripción opcional |
| tiempoPrestamo | Int | Sí | Duración en meses |
| diaPago | Int | Sí | Día del mes para pago |
| tipoInteres | Enum | Sí | PORCENTUAL o FIJO |
| valorInteres | Decimal | Sí | Valor del interés (porcentaje o monto fijo) |
| estado | Enum | Auto | ACTIVO, PAGADO_PARCIAL, PAGADO_TOTAL |
| montoPagado | Decimal | Auto | Suma de pagos realizados |

**Reglas de negocio:**
- Interés solo en préstamos recibidos
- Interés se calcula sobre el monto del préstamo actual (no sobre saldo pendiente)
- Interés puede ser porcentaje o monto fijo mensual
- Se pueden registrar pagos

---

### 2.8 Pago de Préstamo Recibido

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Identificador único |
| prestamoRecibidoId | Long | Sí | FK a préstamo recibido |
| monto | Decimal | Sí | Monto del pago |
| fecha | Date | Sí | Fecha del pago |
| descripcion | String | No | Nota opcional |

---

## 3. Vistas Principales

### 3.1 Registro de Movimientos

- Lista de todos los movimientos (ingresos, gastos, transferencias)
- Filtros por: cuenta, categoría, tipo, rango de fechas
- Acciones: crear nuevo ingreso, crear nuevo gasto, crear transferencia
- Cada movimiento muestra: monto, categoría, cuenta, fecha, descripción
- Indicador visual de presupuesto en movimientos de gasto

### 3.2 Gestión de Préstamos

Dos pestañas/secciones:

**Préstamos otorgados:**
- Lista de personas que me deben
- Acciones: nuevo préstamo, registrar pago recibido
- Detalle con historial de pagos

**Préstamos recibidos:**
- Lista de préstamos que debo
- Acciones: nuevo préstamo/cuotas, registrar pago realizado
- Detalle con historial de pagos

### 3.3 Gestión de Categorías

- Lista de categorías agrupadas por tipo (ingreso/gasto)
- CRUD completo
- Configuración de presupuesto por categoría
- Indicador visual de estado del presupuesto

---

## 4. Configuración

- **Moneda**: configurable, se puede cambiar en cualquier momento
- **Idioma**: español (por defecto del sistema)
- **Tema**: claro/oscuro (sigue preferencia del sistema)
- **Sin autenticación**: la app se abre directamente
- **Sin exportación/reportes**: funcionalidad futura
- **Sin recurrencia**: todos los movimientos son manuales

---

## 5. Decisiones Técnicas

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

## 6. Orden de Implementación

1. **Capa de datos**: Entities, DAOs, Database, TypeConverters
2. **Repositorios**: Implementaciones de acceso a datos
3. **ViewModels**: Lógica de negocio por pantalla
4. **Navegación**: NavDisplay con rutas
5. **Pantallas**: UI de cada vista principal
6. **Presupuestos**: Lógica de alertas e indicadores
7. **Préstamos**: CRUD y cálculos de interés

---

## 7. Verificación

- Compilar sin errores: `./gradlew assembleDebug`
- Verificar Room: tests de DAO con Room testing
- Verificar UI: navegación entre pantallas, CRUD de entidades
- Verificar presupuestos: registrar gasto y ver indicador visual
- Verificar préstamos: crear préstamo, registrar pagos, ver estado
