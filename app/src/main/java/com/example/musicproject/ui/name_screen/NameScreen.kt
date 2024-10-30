package com.example.musicproject.ui.name_screen

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.musicproject.navigation.Screen
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
import com.example.musicproject.viewmodel.name.NameActions
import com.example.musicproject.viewmodel.name.NameState
import com.example.musicproject.viewmodel.name.NameViewModel

@Composable
fun NameScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    nameViewModel: NameViewModel = hiltViewModel(),
) {

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val uiState by nameViewModel.state.collectAsStateWithLifecycle()

    // Handle navigation
    HandleNavigation(
        uiState = uiState,
        navController = navController
    )

    // Content of screen
    NameScreenContent(
        authState = authState,
        authViewModel = authViewModel,
        uiState = uiState,
        nameViewModel = nameViewModel
    )

    Log.e("NameScreen", "UiState: ${authState.isEnableButton}")
}

@Composable
private fun NameScreenContent(
    authState: AuthState,
    authViewModel: AuthViewModel,
    uiState: NameState,
    nameViewModel: NameViewModel,
    horizontalPadding: Dp = 16.dp,
) {

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
                    nameViewModel.onAction(NameActions.OnBack)
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
                    nameViewModel.onAction(NameActions.IsEnableButton(it, authState.surName))
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
                    nameViewModel.onAction(NameActions.IsEnableButton(it, authState.name))
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
                    nameViewModel.onAction(NameActions.OnNavigateTo)
                }
            },
            title = "Tiếp tục",
            isEnable = uiState.isEnableButton,
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
                NavigationUtils.navigateTo(navController, Screen.BirthDayScreen.route, popUp = null)
            }
        }
    }
}
