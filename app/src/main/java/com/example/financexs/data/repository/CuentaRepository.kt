package com.example.financexs.data.repository

import com.example.financexs.data.local.dao.CuentaDao
import com.example.financexs.data.local.entity.toDomain
import com.example.financexs.data.local.entity.toEntity
import com.example.financexs.domain.model.Cuenta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CuentaRepository(private val cuentaDao: CuentaDao) {

    fun getAllCuentas(): Flow<List<Cuenta>> = cuentaDao.getAllCuentas().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getCuentaById(id: Long): Cuenta? = cuentaDao.getCuentaById(id)?.toDomain()

    suspend fun insertCuenta(cuenta: Cuenta): Long = cuentaDao.insertCuenta(cuenta.toEntity())

    suspend fun updateCuenta(cuenta: Cuenta) = cuentaDao.updateCuenta(cuenta.toEntity())

    suspend fun deleteCuenta(cuenta: Cuenta) = cuentaDao.deleteCuenta(cuenta.toEntity())

    suspend fun getMovimientosCountByCuenta(cuentaId: Long): Int = cuentaDao.getMovimientosCountByCuenta(cuentaId)
}
