package com.example.financexs.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.financexs.data.local.converter.Converters
import com.example.financexs.data.local.entity.CategoriaEntity
import com.example.financexs.data.local.entity.CuentaEntity
import com.example.financexs.data.local.entity.MovimientoEntity
import com.example.financexs.data.local.entity.PerfilEntity
import com.example.financexs.data.local.entity.PresupuestoEntity

@Database(
    entities = [
        PerfilEntity::class,
        CuentaEntity::class,
        CategoriaEntity::class,
        MovimientoEntity::class,
        PresupuestoEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun perfilDao(): com.example.financexs.data.local.dao.PerfilDao
    abstract fun cuentaDao(): com.example.financexs.data.local.dao.CuentaDao
    abstract fun categoriaDao(): com.example.financexs.data.local.dao.CategoriaDao
    abstract fun movimientoDao(): com.example.financexs.data.local.dao.MovimientoDao
    abstract fun presupuestoDao(): com.example.financexs.data.local.dao.PresupuestoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "financexs_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
