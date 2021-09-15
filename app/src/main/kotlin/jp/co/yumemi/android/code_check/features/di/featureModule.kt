package jp.co.yumemi.android.code_check.features.di

import jp.co.yumemi.android.code_check.features.domain.GithubUseCase
import jp.co.yumemi.android.code_check.features.screen.search.OneViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureModule = module {
    viewModel { OneViewModel(get()) }
    factory { GithubUseCase(get()) }
}