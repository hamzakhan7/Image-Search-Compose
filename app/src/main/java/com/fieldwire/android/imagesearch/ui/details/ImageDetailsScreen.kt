package com.fieldwire.android.imagesearch.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun ImageDetailsScreen(
    viewModel: ImageDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is ImageDetailsState.Loading -> {
                CircularProgressIndicator()
            }
            is ImageDetailsState.Success -> {
                AsyncImage(
                    model = currentState.image.link,
                    contentDescription = currentState.image.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is ImageDetailsState.Error -> {
                Text(text = currentState.message)
            }
        }

    }
}
