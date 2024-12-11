package com.example.musicproject.ui.password_screen

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Visibility
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
import com.example.musicproject.ui.name_screen.LoadingDialog
import com.example.musicproject.ui.theme.DarkOrange
import com.example.musicproject.ui.theme.DarkWhite
import com.example.musicproject.utils.NavigationUtils
import com.example.musicproject.viewmodel.auth.AuthActions
import com.example.musicproject.viewmodel.auth.AuthState
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.password.PasswordAction
import com.example.musicproject.viewmodel.password.PasswordState
import com.example.musicproject.viewmodel.password.PasswordViewModel

@Composable
fun PasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    passwordViewModel: PasswordViewModel,
) {

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val passwordState by passwordViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(passwordState) {
        Log.e("Password Screen", "State : $passwordState")
    }

    // Content of screen
    ScreenContent(
        authState = authState,
        authViewModel = authViewModel,
        passwordState = passwordState,
        passwordViewModel = passwordViewModel,
    )

    // Failure Dialog
    HandleLoadingFailure(authState.isFailed)

    // Loading Dialog
    HandleLoading(authState.isLoading)

    // Trigger login if necessary
    LaunchedEffect(passwordState.isTrigger) {
        if (passwordState.isTrigger) {
            if (authState.isLogin) {
                authViewModel.onAction(AuthActions.OnShowLoading)
                authViewModel.onAction(AuthActions.Login)
            } else {
                passwordViewModel.onAction(PasswordAction.OnNavigateTo)
            }
            passwordViewModel.onAction(PasswordAction.ResetTrigger)
        }
    }

    LaunchedEffect(authState) {
        when {
            authState.isSuccess -> {
                authViewModel.onAction(AuthActions.OnHideLoading)
                passwordViewModel.onAction(PasswordAction.OnNavigateTo)
            }

            authState.isFailed -> {
                authViewModel.onAction(AuthActions.OnHideLoading)
                passwordViewModel.onAction(PasswordAction.ResetTrigger)
            }
        }
    }

    // Handle navigation
    HandleNavigation(
        navController = navController,
        uiState = passwordState,
        authState = authState,
        passwordViewModel = passwordViewModel
    )

}

@Composable
private fun HandleLoadingFailure(authState: Boolean) {
    if (authState) {
        FailureDialog()
    }
}

@Composable
fun FailureDialog(
    modifier: Modifier = Modifier,
    dialogWidth: Dp = 250.dp,
    dialogHeight: Dp = 250.dp,
    message: String = "Email hoặc mật khẩu không chính xác!",
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
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HandleLoading(authState: Boolean) {
    // Show loading animation based on the loading state
    if (authState) {
        LoadingDialog()
    }
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
                    passwordViewModel.onAction(PasswordAction.OnBack)
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
                onTogglePasswordVisibility = { passwordViewModel.onAction(PasswordAction.IsShowingPassword) },
                onValueChange = {
                    authViewModel.onAction(AuthActions.UpdatePassword(it))
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
                    passwordViewModel.onAction(PasswordAction.IsTrigger)
                }
            },
            title = "Tiếp tục",
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkOrange,
                contentColor = Color.Black,
                disabledContainerColor = Color.DarkGray,
                disabledContentColor = DarkWhite
            ),
            isEnable = authViewModel.enablePasswordButton(),
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
    passwordViewModel: PasswordViewModel,
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
                    if (authState.isLogin) Screen.MainScreen.route else Screen.BirthDayScreen.route
                passwordViewModel.onAction(PasswordAction.ResetTrigger)
                NavigationUtils.navigateTo(navController, target)
            }
        }
    }

}
