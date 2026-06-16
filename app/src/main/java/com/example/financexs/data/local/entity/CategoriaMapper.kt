package com.example.financexs.data.local.entity

import com.example.financexs.domain.model.Categoria

fun CategoriaEntity.toDomain() = Categoria(
    id = id,
    nombre = nombre,
    tipo = tipo,
    icono = icono,
    color = color
)

fun Categoria.toEntity() = CategoriaEntity(
    id = id,
    nombre = nombre,
    tipo = tipo,
    icono = icono,
    color = color
)
