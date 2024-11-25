package com.example.musicproject.viewmodel.birthday

interface DobActions {
    data object OnBack: DobActions
    data object OnNavigateTo: DobActions
    data object UpdateVisibleDatePicker : DobActions
}