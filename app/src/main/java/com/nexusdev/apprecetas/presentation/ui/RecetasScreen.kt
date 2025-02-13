package com.nexusdev.apprecetas.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nexusdev.apprecetas.R
import com.nexusdev.apprecetas.data.local.entity.RecetaEntity
import com.nexusdev.apprecetas.presentation.viewmodel.RecetasViewModel

@Composable
fun RecetasScreen(
    navController: NavController, viewModel: RecetasViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Header()
            Recetas(navController, viewModel)
        }

        FloatingActionButton(
            onClick = { navController.navigate("add") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = colorResource(id = R.color.customGreen)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar receta")
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "Hola, ", fontSize = 16.sp, color = Color.Gray)
            Text(text = "¿Qué deseas cocinar hoy?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Box {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Solo un logo xd",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.Black)
            )
        }
    }
}

@Composable
fun Recetas(navController: NavController, viewModel: RecetasViewModel) {

    val recetas by viewModel.recetas.collectAsState()
    val recetasFav by viewModel.recetasFav.collectAsState()
    val recetasOrder by viewModel.recetasOrdered.collectAsState()

    var isFavorite by remember { mutableStateOf(false) }
    var isOrdered by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val recetasFiltradas = recetas.filter {
        it.titulo.contains(searchText, ignoreCase = true)
        it.descripcion.contains(searchText, ignoreCase = true)
    }

    val recetasFiltradasFav = recetasFav.filter {
        it.titulo.contains(searchText, ignoreCase = true)
        it.descripcion.contains(searchText, ignoreCase = true)
    }

    val recetasFiltradasOrder = recetasOrder.filter {
        it.titulo.contains(searchText, ignoreCase = true)
        it.descripcion.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar receta") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
            )

            Image(
                painter = painterResource(
                    id = if (isFavorite) R.drawable.baseline_bookmark_added else R.drawable.baseline_bookmark_border
                ),
                contentDescription = "Icono favoritos",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { isFavorite = !isFavorite }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = if (isOrdered) R.drawable.baseline_filter_alt_off else R.drawable.baseline_filter_alt
                ),
                contentDescription = "Icono de filtro",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { isOrdered = !isOrdered }
            )

            Spacer(modifier = Modifier.width(5.dp))

            if (isOrdered) {
                Text("Quitar filtro")
            } else {
                Text("Ordenar por tiempos")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isOrdered) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(recetasFiltradasOrder) { recetasOrder ->
                    RecetaCard(recetasOrder, viewModel, navController)
                }
            }
        } else {
            if (isFavorite) {
                if (recetasFiltradasFav.isEmpty()) {
                    Text(
                        text = "Aún no has marcado nada como favorito ☺️",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(recetasFiltradasFav) { recetasFav ->
                            RecetaCard(recetasFav, viewModel, navController)
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(recetasFiltradas) { receta ->
                        RecetaCard(receta, viewModel, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun RecetaCard(receta: RecetaEntity, viewModel: RecetasViewModel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable {
                navController.navigate("detalle/${receta.id}")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(1.dp)
                .align(alignment = Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                AsyncImage(
                    model = receta.imagen,
                    contentDescription = "Imagen de ${receta.titulo}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                if (receta.favorito) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.baseline_bookmark_added
                        ),
                        contentDescription = "Icono favoritos",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .size(40.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = receta.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Tiempo: ${receta.tiempo} min", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))

            /*Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { viewModel.deleteReceta(receta) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                IconButton(onClick = { viewModel.addFavorito(receta.id) }) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favorito",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }*/
        }
    }
}
