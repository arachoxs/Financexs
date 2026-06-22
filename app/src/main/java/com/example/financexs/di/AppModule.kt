package com.example.financexs.di

import android.content.Context
import com.example.financexs.data.local.PreferenciasApp
import com.example.financexs.data.local.database.AppDatabase
import com.example.financexs.data.local.dao.CuentaDao
import com.example.financexs.data.local.dao.CategoriaDao
import com.example.financexs.data.local.dao.PerfilDao
import com.example.financexs.data.local.dao.PresupuestoDao
import com.example.financexs.data.repository.CuentaRepository
import com.example.financexs.data.repository.CategoriaRepository
import com.example.financexs.data.repository.PerfilRepository
import com.example.financexs.data.repository.PresupuestoRepository

object AppModule {

    @Volatile
    private var database: AppDatabase? = null

    @Volatile
    private var preferenciasInstance: PreferenciasApp? = null

    @Synchronized
    fun init(context: Context) {
        if (database == null) {
            database = AppDatabase.getInstance(context)
        }
        if (preferenciasInstance == null) {
            preferenciasInstance = PreferenciasApp(context.applicationContext)
        }
    }

    private fun getDatabase(): AppDatabase = database
        ?: throw IllegalStateException("AppModule.init(context) must be called before using dependencies")

    val preferencias: PreferenciasApp
        get() = preferenciasInstance
            ?: throw IllegalStateException("AppModule.init(context) must be called before using dependencies")

    // DAOs
    val perfilDao: PerfilDao by lazy { getDatabase().perfilDao() }
    val cuentaDao: CuentaDao by lazy { getDatabase().cuentaDao() }
    val categoriaDao: CategoriaDao by lazy { getDatabase().categoriaDao() }
    val presupuestoDao: PresupuestoDao by lazy { getDatabase().presupuestoDao() }

    // Repositories
    val perfilRepository: PerfilRepository by lazy { PerfilRepository(perfilDao) }
    val cuentaRepository: CuentaRepository by lazy { CuentaRepository(cuentaDao) }
    val categoriaRepository: CategoriaRepository by lazy { CategoriaRepository(categoriaDao) }
    val presupuestoRepository: PresupuestoRepository by lazy { PresupuestoRepository(presupuestoDao) }
}
