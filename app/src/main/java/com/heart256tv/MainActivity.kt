package com.heart256tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        bottomBar = {
                            if (currentRoute == "home" || currentRoute == "live_tv" || currentRoute == "settings") {
                                NavigationBar(containerColor = Color.Black) {
                                    NavigationBarItem(
                                        selected = currentRoute == "home",
                                        onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                                        icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = if (currentRoute == "home") Color.Red else Color.Gray) },
                                        label = { Text("Home", color = if (currentRoute == "home") Color.Red else Color.Gray) }
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == "live_tv",
                                        onClick = { navController.navigate("live_tv") { popUpTo("home") { inclusive = true } } },
                                        icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Live TV", tint = if (currentRoute == "live_tv") Color.Red else Color.Gray) },
                                        label = { Text("Live TV", color = if (currentRoute == "live_tv") Color.Red else Color.Gray) }
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == "settings",
                                        onClick = { navController.navigate("settings") { popUpTo("home") { inclusive = true } } },
                                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = if (currentRoute == "settings") Color.Red else Color.Gray) },
                                        label = { Text("Settings", color = if (currentRoute == "settings") Color.Red else Color.Gray) }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController, 
                            startDestination = "home",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("home") {
                                HomeScreen(
                                    onCategoryClick = { category ->
                                        navController.navigate("live_tv")
                                    }
                                )
                            }

                            composable("live_tv") {
                                val uiState by viewModel.uiState.collectAsState()
                                LiveTvScreen(
                                    channels = uiState.channels,
                                    isLoading = uiState.isLoading,
                                    error = uiState.error,
                                    onChannelClick = { videoUrl ->
                                        navController.navigate("player/${android.net.Uri.encode(videoUrl)}")
                                    }
                                )
                            }

                            composable("settings") {
                                SettingsScreen()
                            }

                            composable("player/{videoUrl}") { backStackEntry ->
                                val videoUrl = backStackEntry.arguments?.getString("videoUrl")
                                if (videoUrl != null) {
                                    VideoPlayerScreen(videoUrl = videoUrl)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
