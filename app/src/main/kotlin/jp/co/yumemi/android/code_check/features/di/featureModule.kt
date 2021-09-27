package jp.co.yumemi.android.code_check.features.di

import jp.co.yumemi.android.code_check.features.screen.bookmark.BookMarkViewModel
import jp.co.yumemi.android.code_check.features.screen.detail.DetailViewModel
import jp.co.yumemi.android.code_check.features.screen.login.LoginViewModel
import jp.co.yumemi.android.code_check.features.screen.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { BookMarkViewModel() }
    viewModel { DetailViewModel(get()) }
}