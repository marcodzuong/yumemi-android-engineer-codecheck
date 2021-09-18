package jp.co.yumemi.android.code_check.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDirections
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.features.screen.detail.DetailScreen
import jp.co.yumemi.android.code_check.features.screen.search.SearchScreen
import jp.co.yumemi.android.code_check.features.screen.search.SearchViewModel
import jp.co.yumemi.android.code_check.main.SplashScreen
import org.koin.androidx.compose.getViewModel


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Splash.name) {
        composable(Screens.Splash.name) {
            SplashScreen{
                navController.navigate(Screens.Search.name){
                    popUpTo(Screens.Splash.name) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Screens.Search.name) {
            val viewModel = getViewModel<SearchViewModel>()
            SearchScreen( viewModel){
                val itemJson = Gson().toJson(it)
                navController.navigate(Screens.Detail.name+"?item=$itemJson")
            }
        }
        composable(Screens.Detail.name+"?item={item}"
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("item")?.let {
                val item = Gson().fromJson(it,Item::class.java)
                DetailScreen(item)
            }

        }
    }
}

class Screens {
    object Splash {
        val name = "splash_screen"
    }

    object Search {
        val name = "search_screen"
    }

    object Detail {
        val name = "detail_screen"
    }
}

