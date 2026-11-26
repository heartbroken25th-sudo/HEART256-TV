package com.heart256tv

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. Define the UI State
data class MainUiState(
    val greeting: String = "Hello World from MVVM!"
)

// 2. Create the ViewModel
class MainViewModel : ViewModel() {
    // MutableStateFlow is the private data that the ViewModel can change
    private val _uiState = MutableStateFlow(MainUiState())
    
    // StateFlow is the public, read-only data that the View (UI) can observe
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    // Later, you will add functions here to change the state, like:
    // fun updateGreeting(newText: String) {
    //     _uiState.value = _uiState.value.copy(greeting = newText)
    // }
}
