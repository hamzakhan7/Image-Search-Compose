package com.fieldwire.android.imagesearch.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fieldwire.android.imagesearch.api.ImageDetail

@Composable
fun ImageDetailsScreen(
    viewModel: ImageDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is ImageDetailsState.Loading -> {
            Loading()
        }
        is ImageDetailsState.Success -> {
            ImageDetailsSuccessContent(image = currentState.image)
        }
        is ImageDetailsState.Error -> {
            ImageLoadError(currentState.message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageDetailsSuccessContent(
    image: ImageDetail
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (!image.title.isNullOrBlank()) {
                        Text(text = image.title)
                    }
                }
            )
        }
    ){ paddingValues ->
        AsyncImage(
            model = image.link,
            contentDescription = image.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@Composable
private fun Loading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ImageLoadError(message: String){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}
