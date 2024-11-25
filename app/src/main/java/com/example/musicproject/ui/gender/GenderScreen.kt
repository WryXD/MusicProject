package com.example.musicproject.ui.gender

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
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
import com.example.musicproject.ui.custom_components.BackButton
import com.example.musicproject.ui.custom_components.Title
import com.example.musicproject.utils.NavigationUtils
import com.example.musicproject.viewmodel.auth.AuthActions
import com.example.musicproject.viewmodel.auth.AuthState
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.gender.GenderAction
import com.example.musicproject.viewmodel.gender.GenderData
import com.example.musicproject.viewmodel.gender.GenderViewModel
import com.example.musicproject.viewmodel.gender.GenderState

@Composable
fun GenderScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    genderViewModel: GenderViewModel,
) {
    val uiState by genderViewModel.genderState.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        Log.e("Gender Screen", "State : $uiState")
    }

    // Handle Navigation
    HandleNavigation(
        navController = navController,
        genderState = uiState,
    )

    val genderList = listOf(
        GenderData.Male,
        GenderData.Female,
        GenderData.NonBinary,
        GenderData.Other,
        GenderData.UnderSpecific
    )

    ScreenContent(
        authViewModel = authViewModel,
        genderViewModel = genderViewModel,
        genderList = genderList,
        authState = authState
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ScreenContent(
    authViewModel: AuthViewModel,
    genderViewModel: GenderViewModel,
    authState: AuthState,
    genderList: List<GenderData>,
    horizontalPadding: Dp = 16.dp,
) {
    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        BackButton(
            Modifier.padding(start = horizontalPadding),
            onClick = remember {
                {
                    genderViewModel.onAction(GenderAction.OnBack)
                }
            }
        )

        Column(Modifier.wrapContentSize()) {
            Title(
                title = "Giới tính của bạn là gì?",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(start = horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genderList.forEach { gender ->
                    GenderUI(
                        gender = gender,
                        authState = authState,
                        onClick = {
                            authViewModel.onAction(AuthActions.UpdateCurrentGender(gender.gender))
                            authViewModel.onAction(AuthActions.UpdateGender(gender.gender))
                            genderViewModel.onAction(GenderAction.OnNavigateTo)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
    }
}

@Composable
fun GenderUI(
    gender: GenderData,
    onClick: () -> Unit,
    authState: AuthState,
) {

    val (color, borderColor) =
        when (authState.currentGender?.get(gender.gender) == true) {
            true -> Pair(Color.White, Color.White)
            false -> Pair(Color.DarkGray, Color.DarkGray)
        }

    OutlinedCard(
        Modifier
            .wrapContentSize()
            .clickable {
                onClick()
            },
        colors = CardColors(
            containerColor = Color.Black,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent,
        ),
        border = BorderStroke(
            width = 2.dp,
            color = borderColor
        ),
        shape = RoundedCornerShape(20.dp),

        ) {

        Text(
            text = gender.gender,
            color = color,
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun HandleNavigation(
    navController: NavController,
    genderState: GenderState,
) {
    LaunchedEffect(genderState) {
        when {
            genderState.isNavigate -> {
                NavigationUtils.navigateTo(navController, Screen.NameScreen.route)
            }

            genderState.isBack -> {
                NavigationUtils.navigateBack(navController)
            }
        }
    }
}

