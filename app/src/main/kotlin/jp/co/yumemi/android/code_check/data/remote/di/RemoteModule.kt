package jp.co.yumemi.android.code_check.data.remote.di

import jp.co.yumemi.android.code_check.data.remote.GithubRemote
import org.koin.dsl.module

val remoteModule = module {
    single { GithubRemote() }
}