package com.example.financexs.data.local.entity

import com.example.financexs.domain.model.Presupuesto

fun PresupuestoEntity.toDomain() = Presupuesto(
    id = id,
    categoriaId = categoriaId,
    limite = limite,
    periodo = periodo
)

fun Presupuesto.toEntity() = PresupuestoEntity(
    id = id,
    categoriaId = categoriaId,
    limite = limite,
    periodo = periodo
)
