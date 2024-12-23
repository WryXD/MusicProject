package com.example.musicproject.ui.main.search_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicproject.R
import com.example.musicproject.ui.custom_components.Title
import com.example.musicproject.ui.main.home_screen.MusicImage
import com.example.musicproject.ui.theme.RobotoFont
import com.example.musicproject.viewmodel.main.MainScreenAction
import com.example.musicproject.viewmodel.main.MainViewModel
import com.example.musicproject.viewmodel.main.library.Actions
import com.example.musicproject.viewmodel.main.library.LibraryViewModel
import com.example.musicproject.viewmodel.main.search.SearchAction
import com.example.musicproject.viewmodel.main.search.SearchViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    mainViewModel: MainViewModel,
    libraryViewModel: LibraryViewModel
) {
    val searchQuery by searchViewModel.searchQuery.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {
        SearchAppBar(
            query = searchQuery.query,
            onQueryChange = { searchViewModel.onAction(SearchAction.Search(it)) },
            onSearch = {
                searchViewModel.onAction(SearchAction.Search(it))
            }
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 2.dp)

        Box(
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            if (searchQuery.query.isEmpty()) {
                EmptySearchHistory()
            } else {
                SearchScreenContent(
                    searchViewModel = searchViewModel,
                    mainViewModel = mainViewModel,
                    libraryViewModel = libraryViewModel
                )
            }

        }
    }
}

@Composable
fun EmptySearchHistory() {
    Title(
        title = "Phát nội dung bạn thích",
        style = RobotoFont.bodySmall,
        modifier = Modifier
            .padding(top = 16.dp)
    )
}

@Composable
fun SearchScreenContent(
    searchViewModel: SearchViewModel,
    mainViewModel: MainViewModel,
    libraryViewModel: LibraryViewModel,
) {
    val musicData by searchViewModel.musicData.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        items(musicData.size) { index ->
            val musicItem by lazy { musicData[index] }
            Spacer(modifier = Modifier.height(8.dp))
            MusicImage(
                musicItem,
                onClick = {
                    mainViewModel.onAction(MainScreenAction.ReleaseExoPlayer)
                    libraryViewModel.onAction(Actions.ReleaseExoPlayer)
                    libraryViewModel.onAction(Actions.UpdateVisibleMusicPlayer(false))
                    mainViewModel.onAction(MainScreenAction.UpdateVisibleMusicPlayer(false))
                    mainViewModel.onAction(
                        MainScreenAction.PlayTrack(
                            musicItem.id,
                            musicItem.title,
                            musicItem.artist,
                            musicItem.albumCoverUrl,
                        )
                    )
                },
                mainViewModel
            )
        }
        item {
            Spacer(Modifier.height(64.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    textColor: Color = Color.LightGray,
    color: Color = Color.DarkGray,
    tint: Color = Color.White,
    placeholder: String = "Bạn muốn nghe gì?",
) {

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = {
                    onQueryChange(it)
                },
                onSearch = {
                    onSearch(it)
                },
                expanded = false,
                onExpandedChange = { },
                enabled = true,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_search),
                            contentDescription = null,
                            tint = tint
                        )
                    }
                },
            )
        },
        expanded = false,
        onExpandedChange = {},
        modifier = modifier.fillMaxWidth(),
        colors = SearchBarDefaults.colors(
            containerColor = color
        ),
        shape = RoundedCornerShape(4.dp),
        content = {

        }
    )
}
