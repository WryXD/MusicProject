package com.example.musicproject.viewmodel.birthday

interface Actions {
    data object OnBack: Actions
    data object OnNavigateTo: Actions
    data object OnEnableButton: Actions
    data object UpdateVisibleDatePicker : Actions
}