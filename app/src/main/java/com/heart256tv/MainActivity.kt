package com.heart256tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

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
                            // Only show the bottom bar on the main screens
                            if (currentRoute == "movies" || currentRoute == "live_tv") {
                                NavigationBar(containerColor = Color.Black) {
                                    NavigationBarItem(
                                        selected = currentRoute == "movies",
                                        onClick = { 
                                            navController.navigate("movies") {
                                                popUpTo("movies") { inclusive = true }
                                            }
                                        },
                                        icon = { Icon(Icons.Default.Home, contentDescription = "Movies", tint = if (currentRoute == "movies") Color.Red else Color.Gray) },
                                        label = { Text("Movies", color = if (currentRoute == "movies") Color.Red else Color.Gray) }
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == "live_tv",
                                        onClick = { 
                                            navController.navigate("live_tv") {
                                                popUpTo("movies") { inclusive = true }
                                            }
                                        },
                                        icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Live TV", tint = if (currentRoute == "live_tv") Color.Red else Color.Gray) },
                                        label = { Text("Live TV", color = if (currentRoute == "live_tv") Color.Red else Color.Gray) }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController, 
                            startDestination = "movies",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            
                            // 1. Movies Route
                            composable("movies") {
                                MovieGridScreen(
                                    viewModel = viewModel,
                                    onMovieClick = { movieId ->
                                        navController.navigate("details/$movieId")
                                    }
                                )
                            }

                            // 2. Live TV Route
                            composable("live_tv") {
                                LiveTvScreen(
                                    onChannelClick = { videoUrl ->
                                        navController.navigate("player/${android.net.Uri.encode(videoUrl)}")
                                    }
                                )
                            }

                            // 3. Details Route
                            composable("details/{movieId}") { backStackEntry ->
                                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                                val movie = viewModel.uiState.value.allMovies.find { it.id == movieId }
                                
                                if (movie != null) {
                                    DetailsScreen(
                                        movie = movie,
                                        onBackClick = { navController.popBackStack() },
                                        onWatchClick = {
                                            navController.navigate("player/${android.net.Uri.encode(movie.videoUrl)}")
                                        }
                                    )
                                }
                            }

                            // 4. Video Player Route
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieGridScreen(viewModel: MainViewModel, onMovieClick: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    TextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        placeholder = { Text("Search movies...", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) 
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.filteredMovies) { movie ->
                MovieCard(
                    movie = movie, 
                    isFavorite = uiState.favoriteIds.contains(movie.id),
                    onClick = { onMovieClick(movie.id) },
                    onFavoriteClick = { viewModel.toggleFavorite(movie.id) }
                )
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie, 
    isFavorite: Boolean, 
    onClick: () -> Unit, 
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
            
            Text(
                text = movie.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(8.dp)
            )
        }
    }
}
