package com.example.musicproject.ui.password_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.musicproject.R
import com.example.musicproject.navigation.Screen
import com.example.musicproject.ui.custom_components.AppButton
import com.example.musicproject.ui.custom_components.BackButton
import com.example.musicproject.ui.custom_components.Password
import com.example.musicproject.ui.custom_components.PasswordHint
import com.example.musicproject.ui.custom_components.Placeholder
import com.example.musicproject.ui.custom_components.Title
import com.example.musicproject.ui.theme.DarkOrange
import com.example.musicproject.ui.theme.DarkWhite
import com.example.musicproject.utils.NavigationUtils
import com.example.musicproject.viewmodel.auth.AuthActions
import com.example.musicproject.viewmodel.auth.AuthState
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.password.PasswordActions
import com.example.musicproject.viewmodel.password.PasswordState
import com.example.musicproject.viewmodel.password.PasswordViewModel

@Composable
fun PasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    passwordViewModel: PasswordViewModel = hiltViewModel(),
) {

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val uiState by passwordViewModel.state.collectAsStateWithLifecycle()

    // Handle navigation
    HandleNavigation(
        navController = navController,
        uiState = uiState,
        authState = authState
    )

    // Content of screen
    ScreenContent(
        authState = authState,
        authViewModel = authViewModel,
        passwordState = uiState,
        passwordViewModel = passwordViewModel,
    )
    Log.e("PasswordScreen", "UiState: $uiState")
    Log.e("PasswordScreen", "Password: ${authState.password}")
}

@Composable
private fun ScreenContent(
    authState: AuthState,
    authViewModel: AuthViewModel,
    passwordState: PasswordState,
    passwordViewModel: PasswordViewModel,
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
        // Back button
        BackButton(
            onClick = remember {
                {
                    passwordViewModel.onAction(PasswordActions.OnBack)
                }
            },
            modifier = Modifier.padding(start = horizontalPadding)
        )

        Column(Modifier.wrapContentHeight()) {
            Title(
                title = "Chọn một mật khẩu",
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
            Spacer(Modifier.height(16.dp))
            Password(
                value = authState.password,
                isShowingPassword = passwordState.isShowingPassword,
                onTogglePasswordVisibility = { passwordViewModel.onAction(PasswordActions.IsShowingPassword) },
                onValueChange = {
                    authViewModel.onAction(AuthActions.UpdatePassword(it))
                    passwordViewModel.onAction(PasswordActions.IsEnableButton(it))
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.DarkGray,
                ),
                placeholder = {
                    Placeholder(text = "Mật khẩu")
                },
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .fillMaxWidth()

            )
            Spacer(Modifier.height(16.dp))

            PasswordHint(
                title = stringResource(R.string.passworld_title),
                hint = stringResource(R.string.password_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)
                    .wrapContentWidth()
            )
        }


        Spacer(Modifier.height(16.dp))

        // Button to next screen
        AppButton(
            onClick = remember {
                {
                    passwordViewModel.onAction(PasswordActions.OnNavigateTo)
                }
            },
            title = "Tiếp tục",
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkOrange,
                contentColor = Color.Black,
                disabledContainerColor = Color.DarkGray,
                disabledContentColor = DarkWhite
            ),
            isEnable = passwordState.isEnableButton,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(56.dp)
                .padding(horizontal = horizontalPadding)
        )
    }
}

@Composable
private fun HandleNavigation(
    navController: NavController,
    uiState: PasswordState,
    authState: AuthState,
) {

    LaunchedEffect(uiState) {

        when {
            uiState.isBack -> {
                NavigationUtils.navigateBack(navController)
            }

            uiState.isNavigate -> {
                val target =
                    if (authState.isLogin) Screen.MainScreen.route else Screen.NameScreen.route
                NavigationUtils.navigateTo(navController, target)
            }
        }
    }

}
