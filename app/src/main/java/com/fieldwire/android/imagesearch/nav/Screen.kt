package com.fieldwire.android.imagesearch.nav

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Results : Screen("results/{query}") {
        fun createRoute(query: String) = "results/$query"
    }
    object Details : Screen("details/{imageId}") {
        fun createRoute(imageId: String) = "details/$imageId"
    }
}
