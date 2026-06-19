package com.example.financexs.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.financexs.data.local.entity.PresupuestoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PresupuestoDao {

    @Query("SELECT * FROM presupuestos ORDER BY categoriaId ASC")
    fun getAllPresupuestos(): Flow<List<PresupuestoEntity>>

    @Query("SELECT * FROM presupuestos WHERE categoriaId = :categoriaId")
    fun getPresupuestosByCategoria(categoriaId: Long): Flow<List<PresupuestoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM presupuestos WHERE categoriaId = :categoriaId AND periodo = :periodo AND id != :excludeId)")
    suspend fun existsByCategoriaAndPeriodo(categoriaId: Long, periodo: String, excludeId: Long = 0): Boolean

    @Query("SELECT * FROM presupuestos WHERE id = :id")
    suspend fun getPresupuestoById(id: Long): PresupuestoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPresupuesto(presupuesto: PresupuestoEntity): Long

    @Update
    suspend fun updatePresupuesto(presupuesto: PresupuestoEntity)

    @Delete
    suspend fun deletePresupuesto(presupuesto: PresupuestoEntity)
}
