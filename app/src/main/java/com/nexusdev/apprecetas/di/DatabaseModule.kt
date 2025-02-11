package com.nexusdev.apprecetas.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.nexusdev.apprecetas.data.local.AppDatabase
import com.nexusdev.apprecetas.data.local.dao.RecetaDao
import com.nexusdev.apprecetas.data.repository.RecetaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return databaseBuilder(
            app,
            AppDatabase::class.java,
            "recetas_db"
        ).build()
    }

    @Provides
    fun provideRecetaDao(database: AppDatabase): RecetaDao {
        return database.recetaDao()
    }

    @Provides
    @Singleton
    fun provideRecetaRepository(recetaDao: RecetaDao): RecetaRepository {
        return RecetaRepository(recetaDao)
    }
}
