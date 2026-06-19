package com.example.financexs.data.repository

import com.example.financexs.data.local.dao.PresupuestoDao
import com.example.financexs.data.local.entity.toDomain
import com.example.financexs.data.local.entity.toEntity
import com.example.financexs.data.local.enums.PeriodoPresupuesto
import com.example.financexs.domain.model.Presupuesto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PresupuestoRepository(private val presupuestoDao: PresupuestoDao) {

    fun getAllPresupuestos(): Flow<List<Presupuesto>> =
        presupuestoDao.getAllPresupuestos().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getPresupuestosByCategoria(categoriaId: Long): Flow<List<Presupuesto>> =
        presupuestoDao.getPresupuestosByCategoria(categoriaId).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun existsByCategoriaAndPeriodo(
        categoriaId: Long,
        periodo: PeriodoPresupuesto,
        excludeId: Long = 0
    ): Boolean = presupuestoDao.existsByCategoriaAndPeriodo(
        categoriaId = categoriaId,
        periodo = periodo.name,
        excludeId = excludeId
    )

    suspend fun insertPresupuesto(presupuesto: Presupuesto): Long =
        presupuestoDao.insertPresupuesto(presupuesto.toEntity())

    suspend fun updatePresupuesto(presupuesto: Presupuesto) =
        presupuestoDao.updatePresupuesto(presupuesto.toEntity())

    suspend fun deletePresupuesto(presupuesto: Presupuesto) =
        presupuestoDao.deletePresupuesto(presupuesto.toEntity())
}
