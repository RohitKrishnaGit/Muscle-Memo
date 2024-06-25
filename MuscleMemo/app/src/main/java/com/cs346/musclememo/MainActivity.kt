package com.cs346.musclememo

import android.os.Bundle
import androidx.compose.material3.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cs346.musclememo.navigation.AppNavHost
import com.cs346.musclememo.navigation.BottomNavigationBar
import com.example.compose.MuscleMemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuscleMemoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    MuscleMemoTheme {
        Scaffold(
            bottomBar = { BottomNavigationBar(bottomBarState = bottomBarState, navHostController = navController) }
        ) { innerPadding ->
            // Apply the padding globally to the whole BottomNavScreensController
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavHost(navController = navController, bottomBarState = bottomBarState)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MuscleMemoTheme {
        MainScreen()
    }
}