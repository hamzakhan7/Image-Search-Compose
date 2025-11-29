package com.fieldwire.android.imagesearch.ui.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchResultsScreen(
    query: String,
    viewModel: SearchResultsViewModel = hiltViewModel()
) {
    LaunchedEffect(query) {
        viewModel.onEvent(SearchResultsEvent.Search(query))
    }

    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val currentState = state) {
            is SearchResultsState.Loading -> {
                CircularProgressIndicator()
            }
            is SearchResultsState.Success -> {
                // We'll implement the staggered grid here later
                Text("Found ${currentState.images.size} images")
            }
            is SearchResultsState.Error -> {
                Text(text = currentState.message)
            }
        }
    }
}
