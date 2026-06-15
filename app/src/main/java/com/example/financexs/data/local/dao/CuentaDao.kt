package com.example.financexs.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.financexs.data.local.entity.CuentaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CuentaDao {

    @Query("SELECT * FROM cuentas ORDER BY fechaCreacion DESC")
    fun getAllCuentas(): Flow<List<CuentaEntity>>

    @Query("SELECT * FROM cuentas WHERE id = :id")
    suspend fun getCuentaById(id: Long): CuentaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuenta(cuenta: CuentaEntity): Long

    @Update
    suspend fun updateCuenta(cuenta: CuentaEntity)

    @Delete
    suspend fun deleteCuenta(cuenta: CuentaEntity)

    @Query("SELECT COUNT(*) FROM movimientos WHERE cuentaId = :cuentaId OR cuentaOrigenId = :cuentaId OR cuentaDestinoId = :cuentaId")
    suspend fun getMovimientosCountByCuenta(cuentaId: Long): Int
}
