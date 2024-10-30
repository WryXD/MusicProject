package com.example.musicproject.ui.boarding_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.musicproject.navigation.Screen
import com.example.musicproject.ui.custom_components.AppButton
import com.example.musicproject.ui.theme.BlueLight
import com.example.musicproject.utils.NavigationUtils
import com.example.musicproject.viewmodel.auth.AuthActions
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.boarding.BoardingActions
import com.example.musicproject.viewmodel.boarding.BoardingState
import com.example.musicproject.viewmodel.boarding.BoardingViewModel
import kotlinx.coroutines.delay

@Composable
fun BoardingScreen(
    navController: NavController,
    viewModel: BoardingViewModel = hiltViewModel(),
    authViewModel: AuthViewModel,
) {

    // Collect UI state of boarding screen
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    // Data for boarding screen
    val pages = listOf(
        BoardingData.First, BoardingData.Second, BoardingData.Third
    )

    // Calculate pager size
    val pagerSize = pages.size

    // Create pager state
    val state = rememberPagerState(pageCount = { pagerSize })

    // Auto slide effect
    AutoSlideEffect(isEnable = uiState.isSliding, state, pages)


    // Handle Navigation
    HandleNavigation(
        uiState = uiState,
        navController = navController,
    )

    // BoardingContent
    BoardingContent(
        state = state,
        pages = pages,
        viewModel = viewModel,
        authViewModel = authViewModel
    )

    Log.e("Boarding Screen", "State : $uiState")
}

@Composable
private fun BoardingContent(
    state: PagerState,
    pages: List<BoardingData>,
    viewModel: BoardingViewModel,
    authViewModel: AuthViewModel,
    horizontalPadding: Dp = 16.dp
) {
    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        HorizontalPager(
            state = state,
            beyondViewportPageCount = 1,
            userScrollEnabled = false,
            key = { id ->
                pages[id].image
            }
        ) { index ->
            BoardingPage(pages = pages[index])
        }

        Spacer(Modifier.height(16.dp))

        Column(Modifier.wrapContentSize()) {

            Text(
                text = "Kết Nối Với Tâm Hồn Qua Âm Nhạc",
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .wrapContentWidth(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(32.dp))

            // Button navigation to sign up screen
            AppButton(
                onClick = remember {
                    {
                        authViewModel.onAction(AuthActions.UpdateIsSignUp)
                        viewModel.onAction(BoardingActions.OnNavigateTo)
                    }
                },
                title = "Tạo một tài khoản",
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueLight, contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .align(Alignment.CenterHorizontally)
                    .height(56.dp),
            )

            Spacer(Modifier.height(16.dp))

            // Button to login screen
            AppButton(
                onClick = remember {
                    {
                        authViewModel.onAction(AuthActions.UpdateIsLogin)
                        viewModel.onAction(BoardingActions.OnNavigateTo)
                    }
                },
                title = "Đăng nhập",
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, contentColor = Color.DarkGray
                ),
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .align(Alignment.CenterHorizontally)
                    .height(64.dp),
            )

        }
    }
}

@Composable
private fun HandleNavigation(
    uiState: BoardingState,
    navController: NavController,
) {
    LaunchedEffect(uiState) {
        when {
            uiState.isNavigate -> {
                NavigationUtils.navigateTo(navController, Screen.AuthScreen.route)
            }
        }
    }
}

@Composable
private fun AutoSlideEffect(isEnable: Boolean, state: PagerState, pages: List<BoardingData>) {

    LaunchedEffect(isEnable) {
        while (true) {
            val nextPage = (state.currentPage + 1) % pages.size
            delay(2500)
            state.scrollToPage(nextPage)
        }
    }
}
