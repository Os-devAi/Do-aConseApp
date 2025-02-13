package com.nexusdev.apprecetas.presentation.ui

import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nexusdev.apprecetas.presentation.viewmodel.RecetasViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.nexusdev.apprecetas.R
import com.nexusdev.apprecetas.data.local.entity.RecetaEntity
import okhttp3.internal.format


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecetaScreen(
    navController: NavController,
    recetaId: String,
    viewModel: RecetasViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var recetaGlobal by remember { mutableStateOf<RecetaEntity?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val receta by viewModel.getRecetaById(recetaId.toInt())
            .collectAsState(initial = null)
        var isPlay by remember { mutableStateOf(false) }

        // funcion para reproducir el audio para leer la descrpp
        val tts = remember {
            TextToSpeech(navController.context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    println("TTS inicializado correctamente")
                } else {
                    println("Error al inicializar TTS")
                }
            }
        }


        fun toggleSpeech(text: String) {
            if (isPlay) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                tts.stop()
            }
        }

        receta?.let {
            recetaGlobal = it
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { "" },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.Filled.ArrowBack,
                                    contentDescription = "Regresar"
                                )
                            }
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            if (recetaGlobal?.video == null) {
                                Toast.makeText(
                                    context,
                                    "No hay video disponible",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(recetaGlobal!!.video)
                                )
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            }
                        },
                        containerColor = colorResource(id = R.color.customGreen)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Filled.PlayArrow,
                                contentDescription = "Reproducir en Video",
                                tint = Color.White
                            )
                            Text(
                                "Reproducir en Video",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box() {
                        AsyncImage(
                            model = it.imagen,
                            contentDescription = "Imagen de ${it.titulo}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.titulo,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(50.dp))
                        if (it.favorito) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.baseline_bookmark_added
                                ),
                                contentDescription = "Icono favoritos",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { viewModel.remFavorito(it.id) }
                            )
                        } else {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.baseline_bookmark_border
                                ),
                                contentDescription = "Icono favoritos",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { viewModel.addFavorito(it.id) }
                            )
                        }
                        Image(
                            painter = painterResource(
                                id = R.drawable.baseline_delete_outline
                            ),
                            contentDescription = "Eliminar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    viewModel.deleteReceta(it)
                                    navController.popBackStack()
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tiempo de preparación: ${format("%.2f", it.tiempo)} min",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            isPlay = !isPlay
                            toggleSpeech(it.descripcion)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.customGreen)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = if (isPlay) R.drawable.stop_circle else R.drawable.play_arrow),
                            contentDescription = "Reproducir audio",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    Text(
                        text = if (isPlay) "Detener descripción" else "Reproducir descripción",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)), // Añadir borde
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = it.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                }
            }
        } ?: CircularProgressIndicator()

        // Correcto uso de DisposableEffect para liberar TTS
        DisposableEffect(Unit) {
            onDispose {
                tts.stop()
                tts.shutdown()
            }
        }


        // Funcion para abrir link asdfjañsdkfj
        @Composable
        fun OpenExternalLink(url: String) {
            val context = LocalContext.current

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            context.startActivity(intent)
        }
    }
}