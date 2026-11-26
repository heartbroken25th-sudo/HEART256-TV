package com.heart256tv

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. The Data Models (matching the JSON from TMDB)
data class TmdbResponse(
    val results: List<TmdbMovie>
)

data class TmdbMovie(
    val id: Int,
    val title: String,
    val release_date: String?,
    val poster_path: String?,
    val overview: String?
)

// 2. The API Interface
interface TmdbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        // 👇👇 CHANGE "1234" TO YOUR REAL API KEY LATER 👇👇
        @Query("api_key") apiKey: String = "7d9881e42ce429812d3de037562886a2", 
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): TmdbResponse
}

// 3. The Retrofit Builder
object RetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val api: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }
}
