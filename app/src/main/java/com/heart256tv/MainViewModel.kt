package com.heart256tv

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Update the UI State to hold a list of movies
data class MainUiState(
    val movies: List<Movie> = emptyList()
)

class MainViewModel : ViewModel() {
    
    // Dummy list with real image URLs
    private val dummyMovies = listOf(
        Movie(1, "Stranger Things", "2016", "https://image.tmdb.org/t/p/w500/49WJfeN0moxb9IPfGn8Jt6pBQ2d.jpg"),
        Movie(2, "The Witcher", "2019", "https://image.tmdb.org/t/p/w500/7vjaCdMw15FEbXyLcQ1vCZy5f5A.jpg"),
        Movie(3, "Squid Game", "2021", "https://image.tmdb.org/t/p/w500/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg"),
        Movie(4, "Wednesday", "2022", "https://image.tmdb.org/t/p/w500/9PFonBhy4cQy7Jz20NpMygczOkv.jpg"),
        Movie(5, "Money Heist", "2017", "https://image.tmdb.org/t/p/w500/reEMJA1uzscCbkpeRJeTT2bjqUp.jpg"),
        Movie(6, "Black Mirror", "2011", "https://image.tmdb.org/t/p/w500/7PRddO7z7mcPi21nZTCMGShAyy1.jpg"),
        Movie(7, "Dark", "2017", "https://image.tmdb.org/t/p/w500/apbrbWs8M9lyOpJYU5WXrpFbk1Z.jpg"),
        Movie(8, "The Crown", "2016", "https://image.tmdb.org/t/p/w500/1M876KPjulVwppEpldhdc8V4o68.jpg"),
        Movie(9, "Bridgerton", "2020", "https://image.tmdb.org/t/p/w500/luoKpgVwi1E5nQsi7W0UuKHu2Rq.jpg"),
        Movie(10, "Ozark", "2017", "https://image.tmdb.org/t/p/w500/pCGyPVrI9Fzw1xlWLK1NfXgM8Qy.jpg")
    )

    private val _uiState = MutableStateFlow(MainUiState(movies = dummyMovies))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
}
