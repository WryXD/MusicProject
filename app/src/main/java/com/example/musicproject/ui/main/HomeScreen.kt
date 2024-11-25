package com.example.musicproject.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.musicproject.viewmodel.main.MainScreenAction
import com.example.musicproject.viewmodel.main.MainViewModel
import com.example.musicproject.viewmodel.main.MusicItem

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    val musicData by mainViewModel.musicData.collectAsState()
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
            .imePadding(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(musicData.size) { index ->
            val musicItem by lazy { musicData[index] }
            Spacer(modifier = Modifier.height(8.dp))
            MusicImage(
                musicItem,
                onClick = {
                    mainViewModel.onAction(MainScreenAction.UpdatePlayingState)
                    mainViewModel.onAction(
                        MainScreenAction.PerformMusic(
                            musicItem.title,
                            musicItem.artist,
                            musicItem.albumCoverUrl,
                            musicItem.previewUrl,
                        )
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicImage(
    musicItem: MusicItem,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .wrapContentSize()
            .clickable {
                onClick()
            }
    ) {
        Card(Modifier.wrapContentSize()) {
            GlideImage(
                model = musicItem.albumCoverUrl,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column() {
            Text(
                text = musicItem.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
            Text(
                text = musicItem.artist,
                color = Color.White
            )
        }
    }
}