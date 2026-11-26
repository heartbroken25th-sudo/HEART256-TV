package com.heart256tv

// We added the description field
data class Movie(
    val id: Int,
    val title: String,
    val year: String,
    val posterUrl: String,
    val description: String = "This is an amazing TV show that you must watch on Heart256 TV!"
)
