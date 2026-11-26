package com.heart256tv

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class TvChannel(
    val name: String,
    val url: String,
    val logo: String = "", 
    val programTitle: String = "Live Stream", 
    val timeRange: String = "LIVE"
)

interface GithubApi {
    @GET("channels.json")
    suspend fun getLiveChannels(): List<TvChannel>
}

object GithubClient {
    private const val GITHUB_BASE_URL = "https://raw.githubusercontent.com/heartbroken25th-sudo/HEART256-TV/main/"
    
    val api: GithubApi by lazy {
        Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }
}
