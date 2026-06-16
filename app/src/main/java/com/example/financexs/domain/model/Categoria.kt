package com.example.financexs.domain.model

import com.example.financexs.data.local.enums.TipoCategoria

data class Categoria(
    val id: Long = 0,
    val nombre: String,
    val tipo: TipoCategoria,
    val icono: String,
    val color: String
)
