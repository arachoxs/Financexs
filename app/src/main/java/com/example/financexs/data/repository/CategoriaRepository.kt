package com.example.financexs.data.repository

import com.example.financexs.data.local.dao.CategoriaDao
import com.example.financexs.data.local.entity.toDomain
import com.example.financexs.data.local.entity.toEntity
import com.example.financexs.data.local.enums.TipoCategoria
import com.example.financexs.domain.model.Categoria
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoriaRepository(private val categoriaDao: CategoriaDao) {

    fun getAllCategorias(): Flow<List<Categoria>> = categoriaDao.getAllCategorias().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getCategoriasByTipo(tipo: TipoCategoria): Flow<List<Categoria>> =
        categoriaDao.getCategoriasByTipo(tipo).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getCategoriaById(id: Long): Categoria? = categoriaDao.getCategoriaById(id)?.toDomain()

    suspend fun insertCategoria(categoria: Categoria): Long = categoriaDao.insertCategoria(categoria.toEntity())

    suspend fun updateCategoria(categoria: Categoria) = categoriaDao.updateCategoria(categoria.toEntity())

    suspend fun deleteCategoria(categoria: Categoria) = categoriaDao.deleteCategoria(categoria.toEntity())

    suspend fun getMovimientosCountByCategoria(categoriaId: Long): Int = categoriaDao.getMovimientosCountByCategoria(categoriaId)
}
