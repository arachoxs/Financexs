package com.example.financexs.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.financexs.data.local.enums.TipoMovimiento
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "movimientos",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CuentaEntity::class,
            parentColumns = ["id"],
            childColumns = ["cuentaId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CuentaEntity::class,
            parentColumns = ["id"],
            childColumns = ["cuentaOrigenId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CuentaEntity::class,
            parentColumns = ["id"],
            childColumns = ["cuentaDestinoId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["categoriaId"]),
        Index(value = ["cuentaId"]),
        Index(value = ["cuentaOrigenId"]),
        Index(value = ["cuentaDestinoId"])
    ]
)
data class MovimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tipo: TipoMovimiento,
    val monto: BigDecimal,
    val categoriaId: Long? = null,
    val cuentaId: Long? = null,
    val cuentaOrigenId: Long? = null,
    val cuentaDestinoId: Long? = null,
    val descripcion: String? = null,
    val fecha: LocalDate = LocalDate.now(),
    val fechaRegistro: LocalDateTime = LocalDateTime.now()
)
