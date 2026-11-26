package com.heart256tv

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val allMovies: List<Movie> = emptyList(),
    val searchQuery: String = "",
    val filteredMovies: List<Movie> = emptyList(),
    val favoriteIds: Set<Int> = emptySet()
)

// Changed from ViewModel to AndroidViewModel to get the Application context
class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val prefsRepository = UserPreferencesRepository(application)

    private val dummyMovies = listOf(
        Movie(1, "Stranger Things", "2016", "https://image.tmdb.org/t/p/w500/49WJfeN0moxb9IPfGn8Jt6pBQ2d.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
        Movie(2, "The Witcher", "2019", "https://image.tmdb.org/t/p/w500/7vjaCdMw15FEbXyLcQ1vCZy5f5A.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
        Movie(3, "Squid Game", "2021", "https://image.tmdb.org/t/p/w500/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
        Movie(4, "Wednesday", "2022", "https://image.tmdb.org/t/p/w500/9PFonBhy4cQy7Jz20NpMygczOkv.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
        Movie(5, "Money Heist", "2017", "https://image.tmdb.org/t/p/w500/reEMJA1uzscCbkpeRJeTT2bjqUp.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
        Movie(6, "Black Mirror", "2011", "https://image.tmdb.org/t/p/w500/7PRddO7z7mcPi21nZTCMGShAyy1.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"),
        Movie(7, "Dark", "2017", "https://image.tmdb.org/t/p/w500/apbrbWs8M9lyOpJYU5WXrpFbk1Z.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"),
        Movie(8, "The Crown", "2016", "https://image.tmdb.org/t/p/w500/1M876KPjulVwppEpldhdc8V4o68.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
        Movie(9, "Bridgerton", "2020", "https://image.tmdb.org/t/p/w500/luoKpgVwi1E5nQsi7W0UuKHu2Rq.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"),
        Movie(10, "Ozark", "2017", "https://image.tmdb.org/t/p/w500/pCGyPVrI9Fzw1xlWLK1NfXgM8Qy.jpg", videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4")
    )

    private val _uiState = MutableStateFlow(
        MainUiState(
            allMovies = dummyMovies,
            filteredMovies = dummyMovies
        )
    )
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        // Listen to DataStore for saved favorites and update the UI state
        viewModelScope.launch {
            prefsRepository.favoriteIds.collect { savedIds ->
                _uiState.update { currentState ->
                    currentState.copy(favoriteIds = savedIds)
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { currentState ->
            val filtered = if (query.isBlank()) {
                currentState.allMovies
            } else {
                currentState.allMovies.filter { 
                    it.title.contains(query, ignoreCase = true) 
                }
            }
            currentState.copy(
                searchQuery = query,
                filteredMovies = filtered
            )
        }
    }

    fun toggleFavorite(movieId: Int) {
        _uiState.update { currentState ->
            val newFavorites = if (currentState.favoriteIds.contains(movieId)) {
                currentState.favoriteIds - movieId 
            } else {
                currentState.favoriteIds + movieId 
            }
            
            // Save the new favorites to DataStore in the background
            viewModelScope.launch {
                prefsRepository.saveFavorites(newFavorites)
            }

            currentState.copy(favoriteIds = newFavorites)
        }
    }
}
