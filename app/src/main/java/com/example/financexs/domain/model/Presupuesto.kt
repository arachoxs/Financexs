package com.example.financexs.domain.model

import com.example.financexs.data.local.enums.PeriodoPresupuesto
import java.math.BigDecimal

data class Presupuesto(
    val id: Long = 0,
    val categoriaId: Long,
    val limite: BigDecimal,
    val periodo: PeriodoPresupuesto
)
