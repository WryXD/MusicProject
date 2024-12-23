package com.example.musicproject.ui.main.library

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.musicproject.R
import com.example.musicproject.repositories.music.remote.Song
import com.example.musicproject.ui.main.search_screen.SearchAppBar
import com.example.musicproject.ui.theme.RobotoFont
import com.example.musicproject.viewmodel.main.MainScreenAction
import com.example.musicproject.viewmodel.main.MainViewModel
import com.example.musicproject.viewmodel.main.library.Actions
import com.example.musicproject.viewmodel.main.library.LibraryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LikedSongScreen(
    libraryViewModel: LibraryViewModel,
    mainViewModel: MainViewModel
) {
    val libraryState by libraryViewModel.library.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color.Green,
            Color(0xFF006400),
            Color.Black
        ),
        start = Offset(0f, 0f),
        end = Offset(
            x = 0f,
            y = screenWidth * 0.8f
        )
    )
    val listState = rememberLazyListState()
    Box(
        Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .imePadding()
                .systemBarsPadding()
                .background(gradient)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {

                },
            state = listState,
        ) {
            stickyHeader {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(gradient)
                ) {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_back),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        SearchAppBar(
                            query = "",
                            onQueryChange = {},
                            onSearch = {},
                            placeholder = "Tìm trong mục bài hát đã thích",
                            textColor = Color.DarkGray,
                            color = Color.White,
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(start = 8.dp)

                        )

                        Spacer(Modifier.width(8.dp))

                        SortedMusic(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 8.dp, end = 8.dp)

                        )

                    }

                    Spacer(Modifier.height(56.dp))

                    Text(
                        text = "Bài hát đã thích",
                        style = RobotoFont.bodySmall,
                        color = Color.White,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = "${libraryState.songs.size} bài hát",
                        style = RobotoFont.bodySmall,
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Spacer(Modifier.height(56.dp))
                }

            }

            items(libraryState.songs.size) { index ->
                Spacer(Modifier.height(8.dp))
                val song = libraryState.songs[index]
                val onMusicClick = remember {
                    {
                        mainViewModel.onAction(MainScreenAction.ReleaseExoPlayer)
                        mainViewModel.onAction(MainScreenAction.UpdateVisibleMusicPlayer(false))
                        libraryViewModel.onAction(
                            Actions.PlayPlaylistTrack(index)
                        )
                    }
                }

                SongLibrary(
                    song = song,
                    onClick = onMusicClick
                )
            }

            item {
                Spacer(Modifier.height(144.dp))
            }
        }
    }
}

@Composable
fun SortedMusic(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sắp xếp",
                style = RobotoFont.bodySmall,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentSize()
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongLibrary(
    song: Song,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .wrapContentSize()
            .padding(horizontal = 8.dp)
            .clickable {
                onClick()
            }) {
        Card(Modifier.wrapContentSize()) {
            GlideImage(
                model = song.songAlbumCoverUrl,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(Modifier.fillMaxWidth(0.9f)) {
            Text(
                text = song.songTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
            Text(
                text = song.songArtist, maxLines = 1, color = Color.White
            )
        }

//        val isShow = remember {
//            mutableStateOf(false)
//        }
//
//        IconButton(
//            onClick = { isShow.value = true },
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.more_vert),
//                contentDescription = null,
//                tint = Color.White
//            )
//        }
//        if (isShow.value) {
//            BottomSheetContent(
//                onDismiss = {
//                    isShow.value = false
//                },
//                id = musicItem.id,
//                model = musicItem.albumCoverUrl,
//                title = musicItem.title,
//                artist = musicItem.artist,
//                previewUrl = musicItem.previewUrl,
//                duration = musicItem.duration,
//                mainViewModel = mainViewModel
//            )
//        }
    }
}