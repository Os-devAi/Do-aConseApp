package com.nexusdev.apprecetas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nexusdev.apprecetas.data.local.dao.RecetaDao
import com.nexusdev.apprecetas.data.local.entity.RecetaEntity

@Database(entities = [RecetaEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recetaDao(): RecetaDao
}