package com.nexusdev.apprecetas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.nexusdev.apprecetas.navigation.AppNavigation
import com.nexusdev.apprecetas.ui.theme.DoñaConseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoñaConseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    it.calculateTopPadding()
                    AppNavigation()
                }
            }
        }
    }
}