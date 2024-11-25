package com.example.musicproject.ui.main

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.musicproject.R
import com.example.musicproject.navigation.Screen
import com.example.musicproject.utils.NavigationUtils
import com.example.musicproject.viewmodel.auth.AuthActions
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.main.MainScreenAction
import com.example.musicproject.viewmodel.main.MainViewModel
import com.example.musicproject.viewmodel.main.MusicPlayerState
import com.example.musicproject.viewmodel.password.PasswordAction
import com.example.musicproject.viewmodel.password.PasswordViewModel

@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    passwordViewModel: PasswordViewModel = hiltViewModel(),
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
            }

            false -> NavigationUtils.navigateTo(navController, Screen.BoardingScreen.route)
        }
    }
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val playerState by mainViewModel.musicPlayerData.collectAsStateWithLifecycle()

    // Music player
    MusicPlayer(
        context = LocalContext.current,
        playerState = playerState
    )

    val bottomNavItem by lazy {
        listOf(
            BottomNavItems.Home, BottomNavItems.Search, BottomNavItems.Library
        )
    }

    val onNavigationItemSelected = remember {
        { index: Int -> mainViewModel.onAction(MainScreenAction.UpdateBottomNavId(index)) }
    }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (mediaPlayer, bottomNav) = createRefs()

        Box(Modifier.fillMaxSize()) {

            MainContent(
                mainViewModel = mainViewModel,
                selectedBottomNavigation = uiState.bottomNavId,
                navController = navController,
            )

        }

        MusicPlayerContent(
            playerState = playerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .constrainAs(mediaPlayer) {
                    bottom.linkTo(bottomNav.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        MainBottomNavigation(
            items = bottomNavItem,
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


@Composable
fun MainContent(
    mainViewModel: MainViewModel,
    selectedBottomNavigation: Int,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    LaunchedEffect(selectedBottomNavigation) {
        when (selectedBottomNavigation) {
            1 -> {}
            2 -> {}
        }
    }

    Box(
        modifier = modifier,
    ) {
        key(selectedBottomNavigation) {
            when (selectedBottomNavigation) {
                0 -> HomeScreen(mainViewModel)
                1 -> {}
                2 -> {}
            }
        }

    }
}

@Composable
private fun MainBottomNavigation(
    items: List<BottomNavItems>,
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

                NavigationBarItem(
                    selected = isSelected,
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
) {
    ConstraintLayout(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.DarkGray.copy(alpha = 1f))
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (imageRes, titleRes, artistRes, playButton) = createRefs()
        Card(
            Modifier
                .wrapContentSize()
                .constrainAs(imageRes) {
                    top.linkTo(parent.top, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                }
        ) {
            GlideImage(
                model = playerState.albumCoverUrl,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
            )
        }

        Text(
            text = playerState.title,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 1,
            modifier = Modifier.constrainAs(titleRes) {
                top.linkTo(imageRes.top)
                start.linkTo(imageRes.end, margin = 8.dp)

            }
        )
        Text(
            text = playerState.artist,
            color = Color(0xFFA69C9C),
            fontSize = 14.sp,
            maxLines = 1,
            modifier = Modifier.constrainAs(artistRes) {
                top.linkTo(titleRes.bottom, margin = 4.dp)
                start.linkTo(titleRes.start)
                bottom.linkTo(imageRes.bottom, margin = 8.dp)
            }
        )
        PlayIcon(
            modifier = Modifier.constrainAs(playButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 8.dp)
            },
            onClick = {

            },
            isPlaying = false
        )
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
            modifier = Modifier
                .size(20.dp),
            tint = Color.White,
            contentDescription = null
        )
    }
}

@Composable
fun MusicPlayer(context: Context, playerState: MusicPlayerState) {
    val exoPlayer = remember(playerState.previewUrl) {
        playerState.currentPlayer?.release()

        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(playerState.previewUrl)
            setMediaItem(mediaItem)
            prepare()
        }
    }

    LaunchedEffect(playerState.isPlaying) {
        when(playerState.isPlaying) {
            true -> exoPlayer.play()
            false -> exoPlayer.pause()
        }
    }
}