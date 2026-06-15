package com.example.financexs.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.financexs.data.local.enums.PeriodoPresupuesto
import java.math.BigDecimal

@Entity(
    tableName = "presupuestos",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["categoriaId"])
    ]
)
data class PresupuestoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoriaId: Long,
    val limite: BigDecimal,
    val periodo: PeriodoPresupuesto
)
