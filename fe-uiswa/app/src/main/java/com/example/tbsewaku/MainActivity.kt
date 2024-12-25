package com.example.tbsewaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier  // Tambahkan impor ini untuk Modifier
import com.example.tbsewaku.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                // Meneruskan modifier yang berisi padding
                NavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
