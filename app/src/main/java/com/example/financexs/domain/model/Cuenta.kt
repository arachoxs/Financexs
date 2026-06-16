package com.example.financexs.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Cuenta(
    val id: Long = 0,
    val nombre: String,
    val color: String,
    val icono: String,
    val saldo: BigDecimal = BigDecimal.ZERO,
    val fechaCreacion: LocalDateTime = LocalDateTime.now()
)
