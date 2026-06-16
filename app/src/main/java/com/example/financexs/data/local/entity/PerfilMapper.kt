package com.example.financexs.data.local.entity

import com.example.financexs.domain.model.Perfil

fun PerfilEntity.toDomain() = Perfil(
    id = id,
    nombre = nombre,
    moneda = moneda,
    tema = tema
)

fun Perfil.toEntity() = PerfilEntity(
    id = id,
    nombre = nombre,
    moneda = moneda,
    tema = tema
)
