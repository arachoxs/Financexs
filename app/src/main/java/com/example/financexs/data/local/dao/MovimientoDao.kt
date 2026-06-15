package com.example.financexs.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.financexs.data.local.entity.MovimientoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {

    @Query("SELECT * FROM movimientos ORDER BY fecha DESC, fechaRegistro DESC")
    fun getAllMovimientos(): Flow<List<MovimientoEntity>>

    @Query("SELECT * FROM movimientos WHERE cuentaId = :cuentaId OR cuentaOrigenId = :cuentaId OR cuentaDestinoId = :cuentaId ORDER BY fecha DESC")
    fun getMovimientosByCuenta(cuentaId: Long): Flow<List<MovimientoEntity>>

    @Query("SELECT * FROM movimientos WHERE categoriaId = :categoriaId ORDER BY fecha DESC")
    fun getMovimientosByCategoria(categoriaId: Long): Flow<List<MovimientoEntity>>

    @Query("SELECT * FROM movimientos WHERE id = :id")
    suspend fun getMovimientoById(id: Long): MovimientoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovimiento(movimiento: MovimientoEntity): Long

    @Update
    suspend fun updateMovimiento(movimiento: MovimientoEntity)

    @Delete
    suspend fun deleteMovimiento(movimiento: MovimientoEntity)

    @Query("SELECT COALESCE(SUM(monto), 0) FROM movimientos WHERE tipo = 'INGRESO' AND cuentaId = :cuentaId AND fecha >= :fechaInicio AND fecha <= :fechaFin")
    suspend fun getIngresosByCuentaAndFecha(cuentaId: Long, fechaInicio: java.time.LocalDate, fechaFin: java.time.LocalDate): java.math.BigDecimal

    @Query("SELECT COALESCE(SUM(monto), 0) FROM movimientos WHERE tipo = 'GASTO' AND cuentaId = :cuentaId AND fecha >= :fechaInicio AND fecha <= :fechaFin")
    suspend fun getGastosByCuentaAndFecha(cuentaId: Long, fechaInicio: java.time.LocalDate, fechaFin: java.time.LocalDate): java.math.BigDecimal

    @Query("SELECT COALESCE(SUM(monto), 0) FROM movimientos WHERE tipo = 'GASTO' AND categoriaId = :categoriaId AND fecha >= :fechaInicio AND fecha <= :fechaFin")
    suspend fun getGastosByCategoriaAndFecha(categoriaId: Long, fechaInicio: java.time.LocalDate, fechaFin: java.time.LocalDate): java.math.BigDecimal
}
