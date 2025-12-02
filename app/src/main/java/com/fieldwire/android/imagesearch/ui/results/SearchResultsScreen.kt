package com.fieldwire.android.imagesearch.ui.results

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fieldwire.android.imagesearch.api.ImageDetail

@Composable
fun SearchResultsScreen(
    query: String,
    onImageClick: (String) -> Unit,
    viewModel: SearchResultsViewModel = hiltViewModel()
) {
    LaunchedEffect(query) {
        viewModel.onEvent(SearchResultsEvent.Search(query))
    }

    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is SearchResultsState.Loading -> {
            Loading()
        }
        is SearchResultsState.Success -> {
            if (currentState.images.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No results found for \"$query\"")
                }
            } else {
                SearchResultsContent(
                    query = query,
                    images = currentState.images,
                    isLoadingMore = currentState.isLoadingMore,
                    onLoadMore = { viewModel.onEvent(SearchResultsEvent.LoadMore) },
                    onImageClick = onImageClick
                )
            }
        }
        is SearchResultsState.Error -> {
            SearchError(message = currentState.message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultsContent(
    query: String,
    images: List<ImageDetail>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyStaggeredGridState()

    val lastVisibleItemIndex by remember {
        derivedStateOf { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
    }

    LaunchedEffect(lastVisibleItemIndex) {
        val currentIndex = lastVisibleItemIndex
        if (currentIndex != null && currentIndex >= images.size - 5 && !isLoadingMore) {
            onLoadMore()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Results for \"$query\"") }
            )
        }
    ) { paddingValues ->
        ResultsGrid(
            images = images,
            isLoadingMore = isLoadingMore,
            gridState = gridState,
            onImageClick = onImageClick,
            modifier = modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun ResultsGrid(
    images: List<ImageDetail>,
    isLoadingMore: Boolean,
    gridState: LazyStaggeredGridState,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(images, key = { it.id }) { image ->
            val aspectRatio = if (image.height > 0) image.width.toFloat() / image.height.toFloat() else 1.0f

            AsyncImage(
                model = image.link,
                contentDescription = image.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .clickable { onImageClick(image.id) }
            )
        }

        if (isLoadingMore) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun Loading(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SearchError(message: String){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message)
    }
}
