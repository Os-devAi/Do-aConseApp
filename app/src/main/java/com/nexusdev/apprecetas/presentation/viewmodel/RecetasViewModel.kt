package com.nexusdev.apprecetas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexusdev.apprecetas.data.local.entity.RecetaEntity
import com.nexusdev.apprecetas.data.repository.RecetaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecetasViewModel @Inject constructor(
    private val repository: RecetaRepository
) : ViewModel() {
    val recetas: StateFlow<List<RecetaEntity>> = repository.getAllRecetas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addReceta(
        titulo: String,
        descripcion: String,
        imagen: String,
        tiempo: Double,
        favorito: Boolean,
        video: String? = null
    ) {
        viewModelScope.launch {
            repository.insertReceta(
                RecetaEntity(
                    titulo = titulo,
                    descripcion = descripcion,
                    imagen = imagen,
                    tiempo = tiempo,
                    favorito = favorito,
                    video = video
                )
            )
        }
    }

    fun deleteReceta(receta: RecetaEntity) {
        viewModelScope.launch {
            repository.deleteReceta(receta)
        }
    }
}
