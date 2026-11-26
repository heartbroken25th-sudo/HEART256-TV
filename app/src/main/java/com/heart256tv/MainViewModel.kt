package com.heart256tv

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val channels: List<TvChannel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        fetchChannels()
    }

    fun fetchChannels() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val fetchedChannels = GithubClient.api.getLiveChannels()
                _uiState.update { it.copy(channels = fetchedChannels, isLoading = false) }
            } catch (e: Exception) {
                Log.e("GITHUB_ERROR", "Failed to fetch channels: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = "Failed to load channels.") }
            }
        }
    }
}
