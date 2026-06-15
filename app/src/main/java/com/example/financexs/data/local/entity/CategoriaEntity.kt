package com.example.financexs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financexs.data.local.enums.TipoCategoria

@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val tipo: TipoCategoria,
    val icono: String,
    val color: String
)
