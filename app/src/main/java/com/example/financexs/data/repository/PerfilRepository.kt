package com.example.financexs.data.repository

import com.example.financexs.data.local.dao.PerfilDao
import com.example.financexs.data.local.entity.toDomain
import com.example.financexs.data.local.entity.toEntity
import com.example.financexs.domain.model.Perfil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PerfilRepository(private val perfilDao: PerfilDao) {

    fun getPerfil(): Flow<Perfil?> = perfilDao.getPerfil().map { it?.toDomain() }

    suspend fun savePerfil(perfil: Perfil) = perfilDao.insertPerfil(perfil.toEntity())
}
