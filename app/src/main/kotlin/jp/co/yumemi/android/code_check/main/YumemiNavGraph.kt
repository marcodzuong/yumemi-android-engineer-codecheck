package jp.co.yumemi.android.code_check.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.features.LoginManager
import jp.co.yumemi.android.code_check.features.screen.bookmark.BookMarkScreen
import jp.co.yumemi.android.code_check.features.screen.bookmark.BookMarkViewModel
import jp.co.yumemi.android.code_check.features.screen.browser.BrowserScreen
import jp.co.yumemi.android.code_check.features.screen.detail.DetailScreen
import jp.co.yumemi.android.code_check.features.screen.detail.DetailViewModel
import jp.co.yumemi.android.code_check.features.screen.login.LoginScreen
import jp.co.yumemi.android.code_check.features.screen.login.LoginViewModel
import jp.co.yumemi.android.code_check.features.screen.profile.ProfileScreen
import jp.co.yumemi.android.code_check.features.screen.search.SearchScreen
import jp.co.yumemi.android.code_check.features.screen.search.SearchViewModel
import org.koin.androidx.compose.getViewModel


@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun YumemiNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: String = Screens.Splash.name
) {
    AnimatedNavHost(navController = navController, startDestination = startDestination) {
        composable(Screens.Splash.name) {
            SplashScreen(onNavigate = {
                if (LoginManager.isLogin.value) {
                    navController.navigate(Screens.Search.name){
                        popUpTo(Screens.Splash.name) {
                            inclusive = true
                        }
                    }
                } else {
                    navController.navigate(Screens.Search.name){
                        popUpTo(Screens.Splash.name) {
                            inclusive = true
                        }
                    }
                    navController.navigate(Screens.Login.name){

                    }
                }
            })
        }
        composable(Screens.Login.name, enterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { 1000 })
        }) {
            val viewModel = getViewModel<LoginViewModel>()
            LoginScreen(
                viewModel = viewModel,
                onNavigation = {
                navController.popBackStack()
            })
        }
        composable(Screens.Search.name, enterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { 1000 })
        }) {
            val viewModel = getViewModel<SearchViewModel>()
            SearchScreen(
                viewModel,
                onNavigate = {
                    val itemJson = Gson().toJson(it)
                    navController.navigate(Screens.Detail.name + "?item=$itemJson")
                },
                onClickProfile = {
                    navController.navigate(Screens.Profile.name)
                },
                onClickLogin = {
                    navController.navigate(Screens.Login.name)
                })
        }
        composable(Screens.Detail.name + "?item={item}", enterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { 1000 })
        }) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("item")?.let {
                val viewModel = getViewModel<DetailViewModel>()
                val item = Gson().fromJson(it, Item::class.java)
                DetailScreen(viewModel = viewModel,
                    item =item, onBack = {
                    navController.popBackStack()
                },
                requestLogin = {
                    navController.navigate(Screens.Login.name)
                })
            }

        }
        composable(Screens.Profile.name, enterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { 1000 })
        }) {
            val context = LocalContext.current
            ProfileScreen(
                onBack = {
                    navController.popBackStack()
                },
                onLogOut = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("130095027277-m7mkh4tt7k4av8e0407d6ubckc5r6q2o.apps.googleusercontent.com")
                        .requestEmail()
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    googleSignInClient.signOut()
                    LoginManager.isLogin.value = false
                    FirebaseAuth.getInstance().signOut()
                    navController.popBackStack()
                },
            onShowBookMark = {
                navController.navigate(Screens.BookMark.name)
            })
        }
        composable(Screens.BookMark.name, enterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { 1000 })
        }){
            val viewModel = getViewModel<BookMarkViewModel>()
            BookMarkScreen(viewModel = viewModel,onBack = {
                navController.popBackStack()
            },onClickPage = {
                navController.navigate(Screens.Browser.name + "?url=$it")
            })
        }
        composable(Screens.Browser.name+"?url={url}", enterTransition = { _, _ ->
            slideInHorizontally(initialOffsetX = { 1000 })
        }){navBackStackEntry ->
            navBackStackEntry.arguments?.getString("url")?.let {
                BrowserScreen(url = it,onBack = {
                    navController.popBackStack()
                })
            }

        }
    }
}

class Screens {
    object Splash {
        const val name = "splash_screen"
    }

    object Search {
        const val name = "search_screen"
    }

    object Profile {
        const val name = "profile_screen"
    }

    object Login {
        const val name = "login_screen"
    }

    object Detail {
        const val name = "detail_screen"
    }
    object BookMark {
        const val name = "book_mark"
    }
    object Browser{
        const val name = "browser_screen"
    }
}

