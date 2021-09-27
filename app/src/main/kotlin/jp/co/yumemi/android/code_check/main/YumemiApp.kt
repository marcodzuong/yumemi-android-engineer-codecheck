package jp.co.yumemi.android.code_check.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun YumemiApp(){
    YumemiTheme {
        val navController = rememberAnimatedNavController()
        YumemiNavGraph(navController)
    }
}