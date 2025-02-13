package com.nexusdev.apprecetas.presentation.ui

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nexusdev.apprecetas.presentation.viewmodel.RecetasViewModel
import kotlinx.coroutines.launch
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.compose.ui.res.colorResource
import com.nexusdev.apprecetas.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun AddRecetas(navController: NavController, viewModel: RecetasViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tiempo by remember { mutableStateOf("") }
    var video by remember { mutableStateOf("") }

    // Lanzador para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedImagePath = saveImageToInternalStorage(context, it)
            if (savedImagePath != null) {
                imageUri = Uri.fromFile(File(savedImagePath))
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Selector de Imagen
            SelectableImage(imageUri) { imagePickerLauncher.launch("image/*") }
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = tiempo,
                onValueChange = { tiempo = it },
                label = { Text("Tiempo de duración (min)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = video,
                onValueChange = { video = it },
                label = { Text("Pega la URL del video (Opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val tiempoDouble = tiempo.toDoubleOrNull()
                    if (titulo.isBlank() || descripcion.isBlank() || imageUri == null || tiempoDouble == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Error: Todos los campos son obligatorios y el tiempo debe ser un número válido.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    } else {
                        viewModel.addReceta(
                            titulo,
                            descripcion,
                            imageUri.toString(),  // Guarda la URI como String
                            tiempoDouble,
                            false,
                            if (video.isBlank()) null else video
                        )
                        // Limpiar campos después de guardar
                        titulo = ""
                        descripcion = ""
                        imageUri = null
                        tiempo = ""
                        video = ""

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Receta guardada correctamente",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.customGreen)
                )
            ) {
                Text("Guardar Receta")
            }
        }
    }
}

fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
    return try {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }

        val fileName = "receta_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        file.absolutePath  // Retorna la ruta de la imagen guardada
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

@Composable
fun SelectableImage(imageUri: Uri?, onImageClick: () -> Unit) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            val bitmap = remember(imageUri) {
                val file = File(imageUri.path ?: "")
                if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
            }

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.fillMaxSize()
                )
            } ?: Text("Error al cargar la imagen")
        } else {
            Text("Selecciona la imagen de tu receta")
        }
    }
}
