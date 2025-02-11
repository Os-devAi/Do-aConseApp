package com.nexusdev.apprecetas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recetas")
data class RecetaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val imagen: String,
    val tiempo: Double,
    val favorito: Boolean,
    val video: String? = null
)