package com.example.financexs.data.local.entity

import com.example.financexs.domain.model.Cuenta

fun CuentaEntity.toDomain() = Cuenta(
    id = id,
    nombre = nombre,
    color = color,
    icono = icono,
    saldo = saldo,
    fechaCreacion = fechaCreacion
)

fun Cuenta.toEntity() = CuentaEntity(
    id = id,
    nombre = nombre,
    color = color,
    icono = icono,
    saldo = saldo,
    fechaCreacion = fechaCreacion
)
