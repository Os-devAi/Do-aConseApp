package com.nexusdev.apprecetas.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nexusdev.apprecetas.presentation.viewmodel.RecetasViewModel
import androidx.compose.foundation.lazy.items

@Composable
fun RecetasScreen(navController: NavController, viewModel: RecetasViewModel = viewModel()) {
    val recetas by viewModel.recetas.collectAsState()

    Column {
        Button(onClick = {
            viewModel.addReceta(
                "Nuevo Post",
                "Contenido del nuevo post",
                "https://example.com/imagen.jpg",
                10.0,
                false,
                video = "https://example.com/video.mp4"
            )
        }) {
            Text("Agregar Post")
        }
        LazyColumn {
            items(recetas) { receta ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = receta.titulo)
                    Text(text = receta.descripcion)
                    Text(text = receta.tiempo.toString())
                    IconButton(onClick = { viewModel.deleteReceta(receta) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}