package com.example.musicproject.ui.main.library

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.musicproject.R
import com.example.musicproject.ui.main.home_screen.HeaderImage
import com.example.musicproject.ui.theme.RobotoFont
import com.example.musicproject.viewmodel.main.MainScreenAction
import com.example.musicproject.viewmodel.main.MainViewModel
import com.example.musicproject.viewmodel.main.home.HomeViewModel
import com.example.musicproject.viewmodel.main.library.Actions
import com.example.musicproject.viewmodel.main.library.LibraryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(
    homeViewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    libraryViewModel: LibraryViewModel,
) {
    val userProfile by homeViewModel.uiState.collectAsStateWithLifecycle()
    val createPlaylistState by libraryViewModel.createPlaylistState.collectAsStateWithLifecycle()
    val playlistLibrary by libraryViewModel.library.collectAsStateWithLifecycle()

    val isShowLikedScreen = remember {
        mutableStateOf(false)
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .imePadding()
                .systemBarsPadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {

                },
            userScrollEnabled = true,
            contentPadding = PaddingValues(16.dp)
        ) {
            stickyHeader(content = {
                Column(
                    Modifier
                        .wrapContentSize()
                        .background(Color.Black)
                ) {
                    val showDrawer = remember {
                        {
                            mainViewModel.onAction(MainScreenAction.TriggerShowDrawer)
                        }
                    }
                    Card(
                        Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(),
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(Color.DarkGray)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HeaderImage(
                                homeViewModel,
                                model = userProfile,
                                circleSize = 44.dp,
                                fontSize = 18.sp,
                                onClick = showDrawer
                            )

                            Spacer(Modifier.width(8.dp))

                            LibraryHeader(
                                fontSize = 18.sp
                            )
                            // Show Create Playlist Dialog
                            val isShowCreatePlaylistDialog = remember {
                                {
                                    libraryViewModel.onAction(Actions.OnShowCreatePlaylistDialog)

                                }
                            }
                            CreateNewPlayListButton(
                                onClick = isShowCreatePlaylistDialog
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    HorizontalDivider(
                        Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp, shape = RoundedCornerShape(8.dp)
                            ),
                        thickness = 2.dp
                    )
                }
            })

            item {
                Spacer(Modifier.height(16.dp))
                LikedSongLayout(boxSize = 54.dp) {
                   isShowLikedScreen.value = true
                }
            }

            items(playlistLibrary.playList.size) { index ->
                Spacer(Modifier.height(16.dp))
                val playListName by lazy { playlistLibrary.playList[index].playListName }
                PlayListLayout(
                    playList = playListName,
                    onClick = {
                       Log.e("PlayList", "PlayList clicked, $index")
                    },
                    boxSize = 54.dp
                )
            }

            item {
                Spacer(Modifier.height(108.dp))

            }
        }

        // Create Playlist
        val create = remember {
            {
                libraryViewModel.onAction(
                    Actions.OnCreatePlaylist(
                        createPlaylistState.playlistName,
                        emptyList()
                    )
                )
                libraryViewModel.onAction(Actions.OnHideCreatePlaylistDialog)
            }
        }
        if (createPlaylistState.isShowing) {
            CreateNewPlayListLayout(
                onCreateClick = create,
                onCancelClick = { libraryViewModel.onAction(Actions.OnHideCreatePlaylistDialog) },
                value = createPlaylistState.playlistName,
                onValueChange = {
                    libraryViewModel.onAction(Actions.UpdatePlaylistName(it))
                }

            )
        }

        if (isShowLikedScreen.value){
            LikedSongScreen(
                libraryViewModel = libraryViewModel,
                mainViewModel = mainViewModel
            )
        }
    }
}


@Composable
fun CreateNewPlayListButton(
    onClick: () -> Unit,
) {
    Box(
        Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd
    ) {
        IconButton(
            onClick = { onClick() },
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_add),
                contentDescription = null,
                tint = Color.White
            )
        }

    }
}

@Composable
fun CreateNewPlayListLayout(
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit,
    onCancelClick: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    alpha: Float = 0.7f,
    boxHeight: Dp = 256.dp,
) {
    Box(
        modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = alpha))
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {

            }
    ) {
        Box(
            Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(boxHeight)
            ) {
                Text(
                    text = "Đặt tên cho danh sách phát của bạn",
                    color = Color.White,
                    style = RobotoFont.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(16.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = value,
                    onValueChange = { onValueChange(it) },
                    textStyle = RobotoFont.bodyMedium,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.DarkGray,
                        unfocusedContainerColor = Color.DarkGray,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    placeholder = {
                        Text(
                            text = "Playlist của tôi",
                            color = Color.White,
                            style = RobotoFont.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    maxLines = 2
                )

                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onCancelClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Huỷ"
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = {
                            onCreateClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Tạo"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LikedSongLayout(
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    boxSize: Dp = 20.dp,
    fontSize: TextUnit = 16.sp,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val gradient = Brush.linearGradient(
            colors = listOf(Color.Red, Color.Blue),
            start = Offset(0f, 0f),
            end = Offset(200f, 200f)
        )
        Box(
            modifier
                .wrapContentSize()
                .size(boxSize)
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_favorite),
                contentDescription = null,
                tint = tint
            )
        }

        Spacer(Modifier.width(8.dp))

        Text(
            text = " Bài hát đã thích",
            style = RobotoFont.bodyMedium,
            color = Color.Green,
            fontSize = fontSize
        )
    }

}

@Composable
fun LibraryHeader(
    fontSize: TextUnit = 18.sp,
) {
    Text(
        text = "Thư viện",
        style = RobotoFont.bodyMedium,
        color = Color.White,
        fontSize = fontSize
    )
}

@Composable
fun PlayListLayout(
    modifier: Modifier = Modifier,
    playList: String,
    onClick: () -> Unit,
    boxSize: Dp = 20.dp,
    fontSize: TextUnit = 16.sp,
    tint: Color = Color.White,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier
                .wrapContentSize()
                .size(boxSize)
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_music),
                contentDescription = null,
                tint = tint
            )
        }

        Spacer(Modifier.width(8.dp))

        Text(
            text = playList,
            style = RobotoFont.bodyMedium,
            color = Color.Green,
            fontSize = fontSize
        )
    }

}