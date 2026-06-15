package com.example.financexs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "cuentas")
data class CuentaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val color: String,
    val icono: String,
    val saldo: BigDecimal = BigDecimal.ZERO,
    val fechaCreacion: LocalDateTime = LocalDateTime.now()
)
