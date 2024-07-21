package com.cs346.musclememo

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.compose.material3.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.cs346.musclememo.navigation.AppNavHost
import com.cs346.musclememo.navigation.BottomNavigationBar
import com.cs346.musclememo.navigation.Screen
import com.cs346.musclememo.utils.AppPreferences
import com.example.compose.MuscleMemoTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreferences.setup(applicationContext)
        val permission = Manifest.permission.POST_NOTIFICATIONS
//        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissionLauncher.launch(permission)
//        }

        // Get the FCM token

        enableEdgeToEdge()
        setContent {
            MuscleMemoTheme  {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel)
            val descriptionText = getString(R.string.channelDescription)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.channelid) , name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(AppPreferences.refreshToken != null)) }
    val startRoute = rememberSaveable {(mutableStateOf(if (AppPreferences.refreshToken==null) Screen.Login.route else Screen.Workout.route))}
    val isDarkTheme = remember { mutableStateOf(AppPreferences.darkMode) }

    val returnToLogin by rememberUpdatedState (
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
            val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", "")
            if (refreshToken == null) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                bottomBarState.value = false
            }
        }
    )

    val themeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "DARKMODE") {
            isDarkTheme.value = AppPreferences.darkMode
        }
    }

    LaunchedEffect(Unit) {
        AppPreferences.listen(themeListener)
    }

    MuscleMemoTheme (
        darkTheme = isDarkTheme.value ?: isSystemInDarkTheme()
    ) {
        LaunchedEffect(Unit) {
            AppPreferences.listen(returnToLogin)
            AppPreferences.unListen(returnToLogin)
        }
        Scaffold(
            bottomBar = { BottomNavigationBar(bottomBarState = bottomBarState, navHostController = navController) }
        ) { innerPadding ->
            // Apply the padding globally to the whole BottomNavScreensController
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavHost(navController = navController, bottomBarState = bottomBarState, startDestination = startRoute.value)
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