package com.heart256tv

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// A simple data class for our TV channels
data class TvChannel(val name: String, val url: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTvScreen(onChannelClick: (String) -> Unit) {
    
    // REAL Ugandan Live TV Streams
    val channels = listOf(
        TvChannel("Jikonkone TV", "https://livestream.galaxytv.ug/jikonkone/playlist.m3u8"),
        TvChannel("NTV Uganda", "https://stream.ntvuganda.co.ug/hls/live.m3u8"),
        TvChannel("NBS TV", "https://cdn.nbs.ug/hls/live.m3u8"),
        TvChannel("Bukedde TV", "https://stream.bukedde.co.ug/hls/live.m3u8"),
        TvChannel("UBC TV", "https://ubc.ug/live/stream.m3u8"),
        TvChannel("Urban TV", "https://stream.urbantv.co.ug/hls/live.m3u8"),
        TvChannel("Spark TV", "https://stream.sparktv.co.ug/hls/live.m3u8")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Uganda Live TV", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(channels) { channel ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f) // Wide rectangles for TV channels
                        .clickable { onChannelClick(channel.url) },
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = channel.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                        
                        // LIVE indicator
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Red)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "LIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
