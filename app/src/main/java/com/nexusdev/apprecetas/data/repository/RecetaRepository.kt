package com.nexusdev.apprecetas.data.repository

import com.nexusdev.apprecetas.data.local.dao.RecetaDao
import com.nexusdev.apprecetas.data.local.entity.RecetaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecetaRepository @Inject constructor(private val recetaDao: RecetaDao){

    fun getAllRecetas(): Flow<List<RecetaEntity>> = recetaDao.getRecetas()

    suspend fun insertReceta(receta: RecetaEntity) {
        recetaDao.insertReseta(receta)
    }

    suspend fun deleteReceta(receta: RecetaEntity) {
        recetaDao.deleteReseta(receta)
    }
}