package com.example.musicproject.ui.main

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.musicproject.R
import com.example.musicproject.domain.model.user.UserProfile
import com.example.musicproject.navigation.Screen
import com.example.musicproject.ui.main.home_screen.HeaderImage
import com.example.musicproject.ui.main.home_screen.HomeScreen
import com.example.musicproject.ui.main.library.LibraryScreen
import com.example.musicproject.ui.main.search_screen.SearchScreen
import com.example.musicproject.ui.theme.RobotoFont
import com.example.musicproject.utils.NavigationUtils
import com.example.musicproject.viewmodel.auth.AuthActions
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.main.MainScreenAction
import com.example.musicproject.viewmodel.main.MainViewModel
import com.example.musicproject.viewmodel.main.MusicPlayerState
import com.example.musicproject.viewmodel.main.home.HomeAction
import com.example.musicproject.viewmodel.main.home.HomeViewModel
import com.example.musicproject.viewmodel.main.library.Actions
import com.example.musicproject.viewmodel.main.library.LibraryViewModel
import com.example.musicproject.viewmodel.main.library.MusicLibraryState
import com.example.musicproject.viewmodel.main.search.SearchViewModel
import com.example.musicproject.viewmodel.password.PasswordAction
import com.example.musicproject.viewmodel.password.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    libraryViewModel: LibraryViewModel,
    passwordViewModel: PasswordViewModel,
) {

    LaunchedEffect(Unit) {
        // Reset password state
        passwordViewModel.onAction(PasswordAction.Reset)
        // Reset auth state
        authViewModel.onAction(AuthActions.ResetAuthState)
    }

    val authenticationState by authViewModel.authenticationState.collectAsState()

    LaunchedEffect(authenticationState.isLoggedIn) {
        when (authenticationState.isLoggedIn) {
            true -> {
                Log.e("Main Screen", "User is login in!")
                homeViewModel.onAction(HomeAction.OnFetchUserProfile)
                libraryViewModel.onAction(Actions.InitializeExoPlayer)
                libraryViewModel.onAction(Actions.ListenToLikedSongUpdate)
                libraryViewModel.onAction(Actions.ListenToPlaylistUpdate)
            }

            false -> NavigationUtils.navigateTo(navController, Screen.BoardingScreen.route)
        }
    }
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val playerState by mainViewModel.musicPlayerData.collectAsStateWithLifecycle()
    val userProfile by homeViewModel.uiState.collectAsStateWithLifecycle()
    val library by libraryViewModel.library.collectAsStateWithLifecycle()
    val bottomNavItem by lazy {
        mutableListOf(
            BottomNavItems.Home, BottomNavItems.Search, BottomNavItems.Library
        )
    }

    // check exoplayer is release or not
    LaunchedEffect(playerState.isReleaseExoPlayer, library.isReleaseExoplayer) {
        Log.e("MainScreen", "Player state: ${playerState.isReleaseExoPlayer}")
        Log.e("MainScreen", "library state: ${library.isReleaseExoplayer}")
        when (playerState.isReleaseExoPlayer) {
            true -> {
                mainViewModel.onAction(MainScreenAction.InitializeExoPlayer)
            }

            false -> {
            }
        }

        when (library.isReleaseExoplayer) {
            true -> {
                libraryViewModel.onAction(Actions.InitializeExoPlayer)
            }

            false -> {

            }
        }
    }

    val onNavigationItemSelected = remember {
        { index: Int -> mainViewModel.onAction(MainScreenAction.UpdateBottomNavId(index)) }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(uiState.isDrawerOpen) {
        if (uiState.isDrawerOpen) {
            drawerState.open()
        }

    }

    LaunchedEffect(playerState) {
        Log.e("MainScreen", "Player state: $playerState")
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                Modifier.fillMaxWidth(0.95f),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    ModalDrawerSheetContent(
                        homeViewModel,
                        userProfile
                    )

                }

                HorizontalDivider(
                    modifier = Modifier.shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp)
                    ),
                    color = Color.White
                )
                Spacer(Modifier.height(16.dp))

                // Logout handle
                val onLogout = remember {
                    {
                        authViewModel.onAction(AuthActions.Logout)
                    }
                }
                LogOut(
                    onClick = onLogout
                )
            }
        },
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .clickable {

                }
        ) {
            val (mediaPlayer, bottomNav) = createRefs()

            Box(Modifier.fillMaxSize()) {

                MainNavigation(
                    mainViewModel = mainViewModel,
                    homeViewModel = homeViewModel,
                    searchViewModel = searchViewModel,
                    libraryViewModel = libraryViewModel,
                    selectedBottomNavigation = uiState.bottomNavId,
                )

            }

            val isShow = remember {
                mutableStateOf(false)
            }

            MusicPlayerContent(
                mainViewModel = mainViewModel,
                playerState = playerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .constrainAs(mediaPlayer) {
                        bottom.linkTo(bottomNav.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                isShow.value = true
            }
            if (isShow.value) {
                ShowMusicAndComment(
                    mainViewModel = mainViewModel,
                    onDismiss = {
                        isShow.value = false
                    },
                    id = 0,
                    artist = playerState.artist,
                    model = "",
                    title = playerState.title,
                    playerState = playerState
                )
            }
            val isShowLibrary = remember {
                mutableStateOf(false)
            }

            MusicPlayerLibraryContent(
                playerState = library,
                libraryViewModel = libraryViewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .constrainAs(mediaPlayer) {
                        bottom.linkTo(bottomNav.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }){
                isShowLibrary.value = true
            }

            if (isShowLibrary.value) {
                ShowMusicAndCommentLibrary(
                    libraryViewModel = libraryViewModel,
                    onDismiss = {
                        isShowLibrary.value = false
                    },
                    id = 0,
                    artist = library.artist,
                    model = "",
                    title = library.title,
                    playerState = library
                )
            }

            MainBottomNavigation(items = bottomNavItem,
                currentRoute = uiState.bottomNavId,
                onItemSelected = onNavigationItemSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(bottomNav) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }

    }
}

@Composable
fun ModalDrawerSheetContent(homeViewModel: HomeViewModel, userProfile: UserProfile) {
    HeaderImage(
        homeViewModel = homeViewModel,
        model = userProfile,
        onClick = {

        },
        fontSize = 28.sp,
        circleSize = 48.dp,
    )
    Spacer(Modifier.width(8.dp))

    HeaderName(
        name = userProfile,
    )
}

@Composable
fun LogOut(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val icon = painterResource(id = R.drawable.icon_logout)
        val iconPainter = remember { icon }
        Icon(
            painter = iconPainter,
            contentDescription = null,
            tint = Color.White,
            modifier = modifier.wrapContentSize()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Đăng xuất",
            style = RobotoFont.bodyMedium,
            color = Color.White,
            fontSize = 16.sp
        )
    }

}

@Composable
fun HeaderName(name: UserProfile) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = name.name,
            style = RobotoFont.bodyMedium,
            color = Color.White,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Xem hồ sơ",
            style = RobotoFont.bodySmall,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun MainNavigation(
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    libraryViewModel: LibraryViewModel,
    selectedBottomNavigation: Int,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier,
    ) {
        key(selectedBottomNavigation) {
            when (selectedBottomNavigation) {
                0 -> HomeScreen(mainViewModel, homeViewModel, libraryViewModel)
                1 -> SearchScreen(searchViewModel, mainViewModel, libraryViewModel)
                2 -> LibraryScreen(homeViewModel, mainViewModel, libraryViewModel)
            }
        }

    }
}

@Composable
private fun MainBottomNavigation(
    items: MutableList<BottomNavItems>,
    currentRoute: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(modifier = modifier) {
        NavigationBar(
            containerColor = Color.Black,
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == currentRoute
                val color = if (isSelected) item.selectedIcon else item.unselectedIcon

                NavigationBarItem(selected = isSelected,
                    onClick = { onItemSelected(index) },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.label,
                            tint = color
                        )
                    },
                    label = {
                        Text(
                            text = item.label, color = color
                        )
                    })
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayerContent(
    modifier: Modifier = Modifier,
    playerState: MusicPlayerState,
    mainViewModel: MainViewModel,
    onClick: () -> Unit,
) {
    var isSwipeHandled by remember { mutableStateOf(false) }

    if (playerState.isVisible) {
        ConstraintLayout(
            modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray.copy(alpha = 1f))
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(onClick = {
                    onClick()
                })
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            if (!isSwipeHandled) {
                                if (dragAmount > 0) {
                                    mainViewModel.playNext()
                                } else {
                                    mainViewModel.playPrevious()
                                }
                                isSwipeHandled = true
                            }
                        },
                        onDragEnd = {
                            isSwipeHandled = false
                        }
                    )
                }
        ) {
            val (imageRes, titleRes, artistRes, playButton, musicProgressBar) = createRefs()
            Card(
                Modifier
                    .wrapContentSize()
                    .constrainAs(imageRes) {
                        top.linkTo(parent.top, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                    }) {
                GlideImage(
                    model = playerState.albumCoverUrl,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
            }

            Text(text = playerState.title,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        repeatDelayMillis = 1000,
                        initialDelayMillis = 0,
                        velocity = 50.dp
                    )
                    .constrainAs(titleRes) {
                        top.linkTo(imageRes.top)
                        start.linkTo(imageRes.end, margin = 8.dp)

                    })
            Text(
                text = playerState.artist,
                color = Color(0xFFA69C9C),
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier
                    .constrainAs(artistRes) {
                        top.linkTo(titleRes.bottom, margin = 4.dp)
                        start.linkTo(titleRes.start)
                        bottom.linkTo(imageRes.bottom, margin = 8.dp)
                    }
            )

            val onPauseMusic = remember {
                {
                    mainViewModel.onAction(MainScreenAction.PauseMusic)
                }
            }
            PlayIcon(
                modifier = Modifier
                    .constrainAs(playButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 8.dp)
                    },
                onClick = onPauseMusic,
                isPlaying = playerState.isPlaying
            )
            key(playerState.previewDuration) {
                MusicProgressBar(
                    isPlayAnimation = playerState.isPlaying,
                    previewDuration = playerState.previewDuration,
                    modifier = Modifier
                        .constrainAs(musicProgressBar) {
                            top.linkTo(imageRes.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(imageRes.start)
                            end.linkTo(parent.end, margin = 8.dp)
                        },
                )
            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayerLibraryContent(
    modifier: Modifier = Modifier,
    playerState: MusicLibraryState,
    libraryViewModel: LibraryViewModel,
    onClick: () -> Unit,
) {
    var isSwipeHandled by remember { mutableStateOf(false) }
    if (playerState.isVisible) {
        ConstraintLayout(
            modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray.copy(alpha = 1f))
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(onClick = {
                    onClick()
                })
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            if (!isSwipeHandled) {
                                if (dragAmount > 0) {
                                    libraryViewModel.playNext()
                                } else {
                                    libraryViewModel.playPrevious()
                                }
                                isSwipeHandled = true
                            }
                        },
                        onDragEnd = {
                            isSwipeHandled = false
                        }
                    )
                }
        ) {
            val (imageRes, titleRes, artistRes, playButton, musicProgressBar) = createRefs()
            Card(
                Modifier
                    .wrapContentSize()
                    .constrainAs(imageRes) {
                        top.linkTo(parent.top, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                    }) {
                GlideImage(
                    model = playerState.albumCoverUrl,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
            }

            Text(text = playerState.title,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        repeatDelayMillis = 1000,
                        initialDelayMillis = 0,
                        velocity = 50.dp
                    )
                    .constrainAs(titleRes) {
                        top.linkTo(imageRes.top)
                        start.linkTo(imageRes.end, margin = 8.dp)

                    })
            Text(
                text = playerState.artist,
                color = Color(0xFFA69C9C),
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier
                    .constrainAs(artistRes) {
                        top.linkTo(titleRes.bottom, margin = 4.dp)
                        start.linkTo(titleRes.start)
                        bottom.linkTo(imageRes.bottom, margin = 8.dp)
                    }
            )

            val onPauseMusic = remember {
                {
                    libraryViewModel.onAction(Actions.PauseMusic)
                }
            }
            PlayIcon(
                modifier = Modifier
                    .constrainAs(playButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 8.dp)
                    },
                onClick = onPauseMusic,
                isPlaying = playerState.isPlaying
            )
            key(playerState.previewDuration) {
                MusicProgressBar(
                    isPlayAnimation = playerState.isPlaying,
                    previewDuration = playerState.previewDuration,
                    modifier = Modifier
                        .constrainAs(musicProgressBar) {
                            top.linkTo(imageRes.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(imageRes.start)
                            end.linkTo(parent.end, margin = 8.dp)
                        },
                )
            }
        }
    }
}

@Composable
fun PlayIcon(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClick: () -> Unit,
) {
    val icon =
        if (isPlaying) painterResource(id = R.drawable.pause) else painterResource(id = R.drawable.play_arrow)
    Box(
        modifier
            .size(28.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Icon(
            painter = icon,
            modifier = Modifier.size(20.dp),
            tint = Color.White,
            contentDescription = null
        )
    }
}

@Composable
fun MusicProgressBar(
    modifier: Modifier = Modifier,
    isPlayAnimation: Boolean,
    previewDuration: Int?,
    color: Color = Color.Green,
    backgroundColor: Color = Color.Gray,
) {

    val progress = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
    ) {

        if (previewDuration != null && previewDuration > 0) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.value)
                    .background(color)
            )

            LaunchedEffect(isPlayAnimation) {
                if (isPlayAnimation) {
                    progress.animateTo(
                        targetValue = 1f, animationSpec = tween(
                            durationMillis = ((1f - progress.value) * previewDuration).toInt(),
                            easing = LinearEasing
                        )
                    )
                } else {
                    progress.stop()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowMusicAndComment(
    scaffoldState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismiss: () -> Unit = {},
    id: Long,
    model: String,
    title: String,
    artist: String,
    playerState: MusicPlayerState,
    mainViewModel: MainViewModel,
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        modifier = Modifier.fillMaxSize(),
        sheetState = scaffoldState,
        content = {
            ShowMusicAndCommentLayout(
                mainViewModel,
                id = id,
                model = model,
                title = title,
                artist = artist,
                playerState = playerState

            )
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowMusicAndCommentLayout(
    mainViewModel: MainViewModel,
    playerState: MusicPlayerState,
    id: Long,
    model: String,
    title: String,
    artist: String,
) {
    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {

        Spacer(Modifier.height(54.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Card(Modifier.wrapContentSize()) {
                GlideImage(
                    model = playerState.albumCoverUrl,
                    contentDescription = null,
                    modifier = Modifier.size(300.dp),
                )
            }
        }
        Spacer(Modifier.height(32.dp))

        Text(
            text = "Bạn đang nghe: $title",
            color = Color.White,
            style = RobotoFont.bodyMedium,
            fontSize = 22.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .basicMarquee()
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Được trình bày bởi nghệ sĩ: $artist",
            color = Color.White,
            style = RobotoFont.bodyMedium,
            fontSize = 22.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .basicMarquee()
        )
        Spacer(Modifier.height(32.dp))

        Text(
            text = "Hãy chia sẻ cảm nhận của bạn về bài hát này để nghệ sĩ hiểu thêm về suy nghĩ của bạn nhé!",
            color = Color.White,
            style = RobotoFont.bodyMedium,
            fontSize = 22.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    repeatDelayMillis = 0,
                    initialDelayMillis = 0,
                    velocity = 50.dp
                )
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Tính năng bình luận sẽ được cập nhật " +
                    "trong thời gian tới! Hãy cùng chờ đón nhé!",
            color = Color.White,
            style = RobotoFont.bodyMedium,
            fontSize = 22.sp,
            maxLines = 2,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    repeatDelayMillis = 0,
                    initialDelayMillis = 0,
                    velocity = 50.dp
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowMusicAndCommentLibrary(
    scaffoldState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismiss: () -> Unit = {},
    id: Long,
    model: String,
    title: String,
    artist: String,
    playerState: MusicLibraryState,
    libraryViewModel: LibraryViewModel,
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        modifier = Modifier.fillMaxSize(),
        sheetState = scaffoldState,
        content = {
            ShowMusicAndCommentLibraryLayout(
                libraryViewModel,
                id = id,
                model = model,
                title = title,
                artist = artist,
                playerState = playerState

            )
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowMusicAndCommentLibraryLayout(
    libraryViewModel: LibraryViewModel,
    playerState: MusicLibraryState,
    id: Long,
    model: String,
    title: String,
    artist: String,
) {
    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {

        Spacer(Modifier.height(54.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Card(Modifier.wrapContentSize()) {
                GlideImage(
                    model = playerState.albumCoverUrl,
                    contentDescription = null,
                    modifier = Modifier.size(300.dp),
                )
            }
        }
        Spacer(Modifier.height(32.dp))

        Text(
            text = "Bạn đang nghe: $title",
            color = Color.White,
            style = RobotoFont.bodyMedium,
            fontSize = 22.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .basicMarquee()
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Được trình bày bởi nghệ sĩ: $artist",
            color = Color.White,
            style = RobotoFont.bodyMedium,
            fontSize = 22.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .basicMarquee()
        )
        Spacer(Modifier.height(32.dp))

        Text(
            text = "Hãy chia sẻ cảm nhận của bạn về bài hát này để nghệ sĩ hiểu thêm về suy nghĩ của bạn nhé!",
            color = Color.White,
            style = RobotoFont.bodyMedium,
            fontSize = 22.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    repeatDelayMillis = 0,
                    initialDelayMillis = 0,
                    velocity = 50.dp
                )
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Tính năng bình luận sẽ được cập nhật " +
                    "trong thời gian tới! Hãy cùng chờ đón nhé!",
            color = Color.White,
            style = RobotoFont.bodyMedium,
            fontSize = 22.sp,
            maxLines = 2,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    repeatDelayMillis = 0,
                    initialDelayMillis = 0,
                    velocity = 50.dp
                )
        )
    }
}