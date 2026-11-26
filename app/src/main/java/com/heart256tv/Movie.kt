package com.heart256tv

// We added videoUrl so each movie can have its own video stream
data class Movie(
    val id: Int,
    val title: String,
    val year: String,
    val posterUrl: String,
    val description: String = "This is an amazing TV show that you must watch on Heart256 TV!",
    val videoUrl: String
)
