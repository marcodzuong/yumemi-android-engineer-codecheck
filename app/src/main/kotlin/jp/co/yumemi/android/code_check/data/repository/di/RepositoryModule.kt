package jp.co.yumemi.android.code_check.data.repository.di

import jp.co.yumemi.android.code_check.data.repository.GithubRepositoryImpl
import jp.co.yumemi.android.code_check.data.repository.IGithubRepository
import org.koin.dsl.module

val repositoryModule = module {
   single { GithubRepositoryImpl(get())  }
}