package com.nexusdev.apprecetas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nexusdev.apprecetas.data.local.entity.RecetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceta(reseta: RecetaEntity)

    @Query("SELECT * FROM recetas")
    fun getRecetas(): Flow<List<RecetaEntity>>

    @Query("SELECT * FROM recetas ORDER BY tiempo")
    fun getRecetasOrderBy(): Flow<List<RecetaEntity>>

    @Query("SELECT * FROM recetas WHERE id = :recetaId")
    fun getDetalle(recetaId: Int): Flow<RecetaEntity>

    @Query("SELECT * FROM recetas WHERE favorito = 1")
    fun getFav(): Flow<List<RecetaEntity>>

    @Query("UPDATE recetas SET favorito = 1 WHERE id = :recetaId")
    suspend fun addFav(recetaId: Int)

    @Query("UPDATE recetas SET favorito = 0 WHERE id = :recetaId")
    suspend fun removeFav(recetaId: Int)

    @Delete
    suspend fun deleteReceta(receta: RecetaEntity)

}