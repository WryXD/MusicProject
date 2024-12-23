package com.example.musicproject.ui.main.home_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.musicproject.R
import com.example.musicproject.domain.model.user.UserProfile
import com.example.musicproject.ui.custom_components.Title
import com.example.musicproject.ui.theme.RobotoFont
import com.example.musicproject.viewmodel.main.MainScreenAction
import com.example.musicproject.viewmodel.main.MainViewModel
import com.example.musicproject.viewmodel.main.MusicItem
import com.example.musicproject.viewmodel.main.home.HomeViewModel
import com.example.musicproject.viewmodel.main.library.Actions
import com.example.musicproject.viewmodel.main.library.LibraryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    libraryViewModel: LibraryViewModel
) {
    val musicData by mainViewModel.musicData.collectAsState()
    val uiState by homeViewModel.uiState.collectAsState()
    LazyColumn(
        Modifier
            .fillMaxHeight()
            .background(Color.Black)
            .systemBarsPadding()
            .imePadding(),
        contentPadding = PaddingValues(8.dp)
    ) {

        stickyHeader(content = {
            val onDrawer = remember {
                {
                    mainViewModel.onAction(MainScreenAction.TriggerShowDrawer)
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(8.dp), content = {
                    HeaderImage(
                        homeViewModel = homeViewModel,
                        model = uiState,
                        onClick = onDrawer
                    )
                })
        })

        items(
            musicData.size,
        ) { index ->
            val musicItem = musicData[index]
            Spacer(modifier = Modifier.height(8.dp))
            val onMusicClick = remember {
                {
                    libraryViewModel.onAction(Actions.ReleaseExoPlayer)
                    libraryViewModel.onAction(Actions.UpdateVisibleMusicPlayer(false))
                    mainViewModel.onAction(MainScreenAction.PlayPlaylistTrack(index))
                }
            }
            MusicImage(
                musicItem = musicItem,
                onClick = onMusicClick,
                mainViewModel = mainViewModel
            )
        }

        item{
            Spacer(Modifier.height(58.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HeaderImage(
    homeViewModel: HomeViewModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    model: UserProfile,
    fontSize: TextUnit = 14.sp,
    circleSize: Dp = 32.dp,
) {

    Card(
        Modifier
            .wrapContentSize()
            .clickable {
                onClick()
            },
        shape = CircleShape
    ) {
        if (model.profilePictureUrl.isEmpty()) {
            Box(
                modifier = modifier
                    .size(circleSize)
                    .background(Color(0xFFFFC0CB), shape = CircleShape)
            ) {
                Text(
                    text = homeViewModel.getFirstName().toString(),
                    textAlign = TextAlign.Center,
                    fontSize = fontSize,
                    modifier = modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }

        } else {
            GlideImage(
                model = model.profilePictureUrl,
                contentDescription = null,
                modifier = modifier
                    .size(circleSize)
                    .background(Color(0xFFFFC0CB))
            )
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MusicImage(
    musicItem: MusicItem,
    onClick: () -> Unit,
    mainViewModel: MainViewModel,
) {
    Row(
        Modifier
            .wrapContentSize()
            .clickable {
                onClick()
            }) {
        Card(Modifier.wrapContentSize()) {
            GlideImage(
                model = musicItem.albumCoverUrl,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(Modifier.fillMaxWidth(0.9f)) {
            Text(
                text = musicItem.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
            Text(
                text = musicItem.artist, maxLines = 1, color = Color.White
            )
        }

        val isShow = remember {
            mutableStateOf(false)
        }

        IconButton(
            onClick = { isShow.value = true },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.more_vert),
                contentDescription = null,
                tint = Color.White
            )
        }
        if (isShow.value) {
            BottomSheetContent(
                onDismiss = {
                    isShow.value = false
                },
                id = musicItem.id,
                model = musicItem.albumCoverUrl,
                title = musicItem.title,
                artist = musicItem.artist,
                mainViewModel = mainViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    scaffoldState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit = {},
    id: Long,
    model: String,
    title: String,
    artist: String,
    mainViewModel: MainViewModel,
) {
    ModalBottomSheet(onDismissRequest = {
        onDismiss()
    }, sheetState = scaffoldState, content = {
        BottomSheetContentLayout(
            mainViewModel,
            id = id,
            model = model,
            title = title,
            artist = artist
        )
    })
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BottomSheetContentLayout(
    mainViewModel: MainViewModel,
    id: Long,
    model: String,
    title: String,
    artist: String,
) {
    Column(Modifier.wrapContentSize()) {
        Row(
            Modifier
                .wrapContentSize()
                .padding(horizontal = 8.dp)
        ) {
            Card(Modifier.wrapContentSize()) {
                GlideImage(
                    model = model,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = title,
                    maxLines = 1,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.basicMarquee()
                )
                Text(
                    text = artist,
                    maxLines = 1,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.basicMarquee()
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(14.dp))
        // add to liked
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clickable {
                    mainViewModel.onAction(
                        MainScreenAction.AddLikedSong(
                            id = id,
                            title = title,
                            artist = artist,
                            albumCoverUrl = model,
                        )
                    )
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(R.drawable.icon_favorite),
                contentDescription = null,
                tint = Color.Red
            )

            Spacer(modifier = Modifier.width(8.dp))

            Title(
                title = "Thêm vào Bài hát đã thích",
                modifier = Modifier.fillMaxWidth(),
                style = RobotoFont.bodySmall,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // add to playlist
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clickable {
                    //
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(R.drawable.icon_add), contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Title(
                title = "Thêm vào danh sách phát",
                modifier = Modifier.fillMaxWidth(),
                style = RobotoFont.bodySmall,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

    }
}


