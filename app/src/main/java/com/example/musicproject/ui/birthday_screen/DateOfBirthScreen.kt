package com.example.musicproject.ui.birthday_screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.musicproject.R
import com.example.musicproject.ui.custom_components.AppButton
import com.example.musicproject.ui.custom_components.BackButton
import com.example.musicproject.ui.custom_components.Title
import com.example.musicproject.ui.theme.DarkOrange
import com.example.musicproject.ui.theme.DarkWhite
import com.example.musicproject.utils.NavigationUtils
import com.example.musicproject.viewmodel.auth.AuthActions
import com.example.musicproject.viewmodel.auth.AuthState
import com.example.musicproject.viewmodel.auth.AuthViewModel
import com.example.musicproject.viewmodel.birthday.Actions
import com.example.musicproject.viewmodel.birthday.DateOfBirthViewModel
import com.example.musicproject.viewmodel.birthday.UiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateOfBirthScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    dobViewModel: DateOfBirthViewModel = hiltViewModel(),
) {

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val uiState by dobViewModel.uiState.collectAsStateWithLifecycle()
    // Handle navigation
    HandleNavigation(
        uiState = uiState,
        navController = navController
    )
    // Content of screen
    ScreenContent(
        authState = authState,
        uiState = uiState,
        authViewModel = authViewModel,
        dobViewModel = dobViewModel,
    )
    Log.e("DateOfBirthScreen", "UiState: $uiState")
}

@Composable
private fun ScreenContent(
    authState: AuthState,
    uiState: UiState,
    authViewModel: AuthViewModel,
    dobViewModel: DateOfBirthViewModel,
    horizontalPadding: Dp = 16.dp,
) {

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        BackButton(
            modifier =
            Modifier.padding(start = horizontalPadding),
            onClick = remember {
                {
                    dobViewModel.onAction(Actions.OnBack)
                }
            }
        )

        // Date picker handle
        DatePickerModalInput(
            visible = uiState.isVisibleDatePicker,
            onDateSelected = remember {
                { date ->
                    date?.let {
                        val formater = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val result = formater.format(Date(it)).toString()
                        authViewModel.onAction(AuthActions.UpdateBirthday(result))
                        dobViewModel.onAction(Actions.OnEnableButton)
                    }
                }
            },
            onDismiss = remember {
                {
                    dobViewModel.onAction(Actions.UpdateVisibleDatePicker)
                }
            }
        )

        Column(Modifier.wrapContentSize()) {
            Title(
                title = "Ngày sinh của bạn là gì?",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = horizontalPadding)
            )

            Spacer(Modifier.height(32.dp))

            // Format date handle
            FormatDayOfBirth(
                onClick = remember {
                    {
                        dobViewModel.onAction(Actions.UpdateVisibleDatePicker)
                    }
                },
                formatDay = authState.birthday ?: "dd/MM/yyyy",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(16.dp))

        // Button to next screen
        AppButton(
            onClick = {

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
private fun HandleNavigation(uiState: UiState, navController: NavController) {

    LaunchedEffect(uiState) {
        when {
            uiState.isBack -> {
                NavigationUtils.navigateBack(navController)
            }
        }
    }
}

@Composable
private fun FormatDayOfBirth(
    modifier: Modifier = Modifier,
    formatDay: String,
    onClick: () -> Unit,
    fontSize: TextUnit = 24.sp,
    horizontalPadding: Dp = 16.dp,
    boxHeight: Dp = 50.dp,
    boxWidth: Dp = 280.dp,
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier
                .width(boxWidth)
                .height(boxHeight)
                .padding(horizontal = horizontalPadding),
            border = BorderStroke(
                width = 1.dp,
                color = Color.White,
            ),
            shape = RoundedCornerShape(5.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = Color.DarkGray,
            )
        ) {

            Row(
                Modifier
                    .wrapContentSize()
                    .height(boxHeight),
                verticalAlignment = Alignment.CenterVertically,

                ) {

                Icon(
                    painter = painterResource(R.drawable.baseline_calendar_month_24),
                    contentDescription = "Calendar",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 4.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = formatDay,
                    fontSize = fontSize,
                )

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    visible: Boolean,
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    if (visible) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
