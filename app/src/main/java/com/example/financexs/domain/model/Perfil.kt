package com.example.financexs.domain.model

import com.example.financexs.data.local.enums.TemaApp

data class Perfil(
    val id: Int = 1,
    val nombre: String,
    val moneda: String,
    val tema: TemaApp = TemaApp.SISTEMA
)
