package com.nexusdev.apprecetas.presentation.ui

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nexusdev.apprecetas.R
import com.nexusdev.apprecetas.presentation.viewmodel.RecetasViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, viewModel: RecetasViewModel = hiltViewModel()) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        val errorMessage by remember { mutableStateOf<String?>(null) }
        var password = remember { mutableStateOf("") }
        val context = LocalContext.current

        Image(painterResource(R.drawable.logo_splash), contentDescription = "logo")

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Text("Login", style = MaterialTheme.typography.titleLarge)

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            TextField(
                value = email,
                singleLine = true,
                onValueChange = { input ->
                    email = input
                },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                isError = password.value.isNotEmpty() && password.value.length < 8,
                colors = TextFieldDefaults.colors()
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        //Configurar shared preferences
        fun guardarSecion(context: Context, isLoggedIn: Boolean) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedIn", isLoggedIn)
                apply()
            }
        }

        Button(
            onClick = {
                if (email == "info@koalit.dev" && password.value == "koalit123") {
                    guardarSecion(context = navController.context, isLoggedIn = true)
                    navController.navigate("recetas") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.customGreen)
            )
        ) {
            Text("Login")
        }
    }
}