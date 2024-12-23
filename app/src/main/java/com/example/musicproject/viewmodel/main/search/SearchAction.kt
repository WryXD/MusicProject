package com.example.musicproject.viewmodel.main.search

interface SearchAction {
    data class Search(val query: String): SearchAction
    data object ExoPlayerState: SearchAction

}