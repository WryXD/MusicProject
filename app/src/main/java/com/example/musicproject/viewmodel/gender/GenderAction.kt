package com.example.musicproject.viewmodel.gender

interface GenderAction {
    data object OnBack : GenderAction
    data object OnNavigateTo : GenderAction
    data object OnSelected : GenderAction

}