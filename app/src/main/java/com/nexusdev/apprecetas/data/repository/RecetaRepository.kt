package com.nexusdev.apprecetas.data.repository

import com.nexusdev.apprecetas.data.local.dao.RecetaDao
import com.nexusdev.apprecetas.data.local.entity.RecetaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RecetaRepository @Inject constructor(private val recetaDao: RecetaDao) {

    fun getAllRecetas(): Flow<List<RecetaEntity>> = recetaDao.getRecetas()

    fun getDetalle(recetaId: Int): Flow<RecetaEntity> = recetaDao.getDetalle(recetaId)

    fun getFav(): Flow<List<RecetaEntity>> = recetaDao.getFav()

    suspend fun insertReceta(receta: RecetaEntity) {
        recetaDao.insertReceta(receta)
    }

    suspend fun addFav(receta: Int) {
        recetaDao.addFav(receta)
    }

    suspend fun remFav(receta: Int) {
        recetaDao.removeFav(receta)
    }

    suspend fun deleteReceta(receta: RecetaEntity) {
        recetaDao.deleteReceta(receta)
    }
}