package com.example.musicproject.ui.auth_screen

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.example.musicproject.ui.custom_components.AppPolicy
import com.example.musicproject.ui.custom_components.BackButton
import com.example.musicproject.ui.custom_components.Email
import com.example.musicproject.ui.custom_components.Placeholder
import com.example.musicproject.ui.custom_components.Title
import com.example.musicproject.ui.theme.DarkOrange
import com.example.musicproject.ui.theme.DarkWhite
import com.example.musicproject.ui.theme.RobotoFont
import com.example.musicproject.utils.NavigationUtils
import com.example.musicproject.viewmodel.auth.AuthActions
import com.example.musicproject.viewmodel.auth.AuthEvents
import com.example.musicproject.viewmodel.auth.AuthState
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.auth.UiState

@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    // auth state
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    // ui state
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    // auth event
    val authEvent by authViewModel.authEventsFlow.collectAsStateWithLifecycle(initialValue = null)
    Log.e("Auth State", "State : $authState")
    Log.e("UI State", "State : $uiState")
    Log.e("Auth Screen", "Is Sign Up: ${authState.isSignUp}")
    Log.e("Auth Screen", "Is Login : ${authState.isLogin}")

    // Handle event
    HandleEvent(
        authEvent = authEvent,
        navController = navController,
        authViewModel = authViewModel

    )

    // Handle navigation
    HandleNavigation(
        uiState = uiState,
        navController = navController,
        authViewModel = authViewModel
    )


    // Content of screen
    ScreenContent(
        authState = authState,
        authViewModel = authViewModel,
    )


}

@Composable
private fun ScreenContent(
    authState: AuthState,
    authViewModel: AuthViewModel,
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

        BackButton(
            onClick = remember {
                {
                    authViewModel.onAction(AuthActions.OnBack)
                }
            },
            modifier = Modifier
                .padding(horizontal = horizontalPadding)

        )
        // Input email field
        Column(Modifier.wrapContentSize()) {
            Title(
                title = "Email của bạn là gì?",
                modifier = Modifier.align(Alignment.CenterHorizontally)

            )

            Spacer(Modifier.height(24.dp))

            Email(
                value = authState.email,
                onValueChange = {
                    authViewModel.onAction(AuthActions.UpdateEmail(it))
                    authViewModel.onAction(AuthActions.IsSignUpValid(it))
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.DarkGray
                ),
                placeholder = remember {
                    {
                        Placeholder(text = "Địa chỉ email")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)

            )
        }

        // Button and policy field
        Column(Modifier.wrapContentSize()) {
            AppPolicy(
                title = stringResource(R.string.policy_title),
                policy1 = stringResource(R.string.policy_1),
                policy2 = stringResource(R.string.policy_2),
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
            )

            Spacer(Modifier.height(8.dp))

            AppButton(
                onClick = remember {
                    {
                        authViewModel.onAction(AuthActions.OnNavigateTo)
                    }
                },
                title = "Tiếp tục",
                isEnable = authState.isEnableButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkOrange,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.DarkGray,
                    disabledContentColor = DarkWhite
                ),
                style = RobotoFont.bodySmall,
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .fillMaxWidth()
                    .height(56.dp)
            )
        }
    }
}

@Composable
private fun HandleEvent(
    authEvent: AuthEvents?,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    LaunchedEffect(authEvent) {
        when (authEvent) {
            AuthEvents.Error -> {
                // show error
            }

            AuthEvents.Loading -> {
                // show loading
            }

            AuthEvents.Success -> {
                // show success
            }

            null -> {
                // do nothing
            }
        }
    }
}

@Composable
private fun HandleNavigation(
    uiState: UiState,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    LaunchedEffect(uiState) {

        when {
            uiState.isNavigate -> {
                NavigationUtils.navigateTo(navController, Screen.PasswordScreen.route)
                authViewModel.onAction(AuthActions.UpdateNavigationStatus)
            }

            uiState.isBack -> {
                authViewModel.onAction(AuthActions.Reset)
                NavigationUtils.navigateBack(navController)
            }
        }
    }

}
