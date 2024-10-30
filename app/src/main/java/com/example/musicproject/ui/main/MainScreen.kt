package com.example.musicproject.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Text(text = "Main Screen")
        }
    }
}