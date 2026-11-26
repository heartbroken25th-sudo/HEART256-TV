package com.heart256tv

// We added posterUrl so the app knows where to download the image from
data class Movie(
    val id: Int,
    val title: String,
    val year: String,
    val posterUrl: String
)
