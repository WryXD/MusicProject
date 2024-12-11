package com.example.musicproject.ui.name_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.musicproject.navigation.Screen
import com.example.musicproject.ui.animation.LoadingAnimation
import com.example.musicproject.ui.custom_components.AppButton
import com.example.musicproject.ui.custom_components.BackButton
import com.example.musicproject.ui.custom_components.NameOrSurName
import com.example.musicproject.ui.custom_components.Placeholder
import com.example.musicproject.ui.custom_components.Title
import com.example.musicproject.ui.theme.DarkOrange
import com.example.musicproject.ui.theme.DarkWhite
import com.example.musicproject.utils.NavigationUtils
import com.example.musicproject.viewmodel.auth.AuthActions
import com.example.musicproject.viewmodel.auth.AuthState
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.name.NameAction
import com.example.musicproject.viewmodel.name.NameState
import com.example.musicproject.viewmodel.name.NameViewModel

@Composable
fun NameScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    nameViewModel: NameViewModel,
) {

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val uiState by nameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
         Log.e("Name Screen", "State : $uiState")
    }

    Box(Modifier.fillMaxSize()) {

        // Handle navigation
        HandleNavigation(
            uiState = uiState,
            navController = navController
        )

        // Content of screen
        NameScreenContent(
            authState = authState,
            authViewModel = authViewModel,
            nameViewModel = nameViewModel
        )

        // Loading Dialog
        if (authState.isLoading) {
            LoadingDialog()
        }

        // Handle auth state changes
        when {
            authState.isSuccess -> {
                nameViewModel.onAction(NameAction.OnNavigateTo)
                authViewModel.onAction(AuthActions.OnHideLoading)
            }
            authState.isFailed -> {
                authViewModel.onAction(AuthActions.OnHideLoading)
            }
        }
    }
}

@Composable
private fun NameScreenContent(
    authState: AuthState,
    authViewModel: AuthViewModel,
    nameViewModel: NameViewModel,
    horizontalPadding: Dp = 16.dp,
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .systemBarsPadding()
                .imePadding(),
            verticalArrangement = Arrangement.SpaceAround
        ) {

            // Back button to previous screen
            BackButton(
                onClick = remember {
                    {
                        nameViewModel.onAction(NameAction.OnBack)
                    }

                },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )

            // Input user name field
            Column(Modifier.wrapContentHeight()) {
                Title(
                    title = "Tên của bạn là gì?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                        .padding(horizontal = horizontalPadding)
                )

                Spacer(Modifier.height(24.dp))

                // Input Name
                NameOrSurName(
                    value = authState.name,
                    onValueChange = {
                        authViewModel.onAction(AuthActions.UpdateName(it))
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.DarkGray,
                        unfocusedContainerColor = Color.DarkGray,
                        focusedIndicatorColor = Color.DarkGray,
                        unfocusedIndicatorColor = Color.DarkGray,
                    ),
                    placeholder = {
                        Placeholder(text = "Họ")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                )

                Spacer(Modifier.height(16.dp))

                // Input sur name
                NameOrSurName(
                    value = authState.surName,
                    onValueChange = {
                        authViewModel.onAction(AuthActions.UpdateSurName(it))
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.DarkGray,
                        unfocusedContainerColor = Color.DarkGray,
                        focusedIndicatorColor = Color.DarkGray,
                        unfocusedIndicatorColor = Color.DarkGray,
                    ),
                    placeholder = {
                        Placeholder(text = "Tên")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Button to next screen
            AppButton(
                onClick = remember {
                    {
                        authViewModel.onAction(AuthActions.OnShowLoading)
                        authViewModel.onAction(AuthActions.Register)
                    }
                },
                title = "Tạo tài khoản",
                isEnable = authViewModel.enableNameButton(),
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkOrange,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.DarkGray,
                    disabledContentColor = DarkWhite
                )
            )
        }
    }
}

@Composable
fun LoadingDialog(
    modifier: Modifier = Modifier,
    dialogWidth: Dp = 150.dp,
    dialogHeight: Dp = 100.dp
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.5f))
            .clickable(
                enabled = false,
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .size(width = dialogWidth, height = dialogHeight)
                .align(Alignment.Center)
                .padding(16.dp),
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation()
            }

        }
    }
}

@Composable
private fun HandleNavigation(
    uiState: NameState,
    navController: NavController,
) {
    LaunchedEffect(uiState) {
        when {
            uiState.isBack -> {
                NavigationUtils.navigateBack(navController)
            }

            uiState.isNavigate -> {
                // Todo navigate to next screen
                NavigationUtils.navigateTo(navController, Screen.MainScreen.route)
            }
        }
    }
}

