package com.example.financexs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financexs.data.local.enums.TemaApp

@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey val id: Int = 1,
    val nombre: String,
    val moneda: String,
    val tema: TemaApp = TemaApp.SISTEMA
)
