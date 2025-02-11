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
    suspend fun insertReseta(reseta: RecetaEntity)

    @Query("SELECT * FROM recetas")
    fun getRecetas(): Flow<List<RecetaEntity>>

    @Delete
    suspend fun deleteReseta(reseta: RecetaEntity)

}