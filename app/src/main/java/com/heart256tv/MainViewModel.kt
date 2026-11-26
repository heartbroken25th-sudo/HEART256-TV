package com.heart256tv

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. Update the UI State to hold a list of movies
data class MainUiState(
    val movies: List<Movie> = emptyList()
)

// 2. Create the ViewModel
class MainViewModel : ViewModel() {
    
    // Create a dummy list of movies for the grid
    private val dummyMovies = listOf(
        Movie(1, "Stranger Things", "2016"),
        Movie(2, "The Witcher", "2019"),
        Movie(3, "Squid Game", "2021"),
        Movie(4, "Wednesday", "2022"),
        Movie(5, "Money Heist", "2017"),
        Movie(6, "Black Mirror", "2011"),
        Movie(7, "Dark", "2017"),
        Movie(8, "The Crown", "2016"),
        Movie(9, "Bridgerton", "2020"),
        Movie(10, "Ozark", "2017")
    )

    private val _uiState = MutableStateFlow(MainUiState(movies = dummyMovies))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
}
