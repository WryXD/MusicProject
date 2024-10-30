package com.example.musicproject.ui.boarding_screen

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.example.musicproject.R

@Immutable
sealed class BoardingData(
    @DrawableRes val image: Int,
) {
    data object First : BoardingData(R.drawable.illu_1)
    data object Second : BoardingData(R.drawable.illu_2)
    data object Third : BoardingData(R.drawable.illu_3)
}
