package com.fieldwire.android.imagesearch.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fieldwire.android.imagesearch.ui.results.SearchResultsScreen
import com.fieldwire.android.imagesearch.ui.search.SearchScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Search.route){
        composable(Screen.Search.route){
            SearchScreen {
                navController.navigate(Screen.Results.createRoute(it))
            }
        }
        composable(
            route = Screen.Results.route,
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(query = query)
        }
    }
}