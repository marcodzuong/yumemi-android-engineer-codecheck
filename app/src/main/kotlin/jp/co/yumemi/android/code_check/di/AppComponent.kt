package jp.co.yumemi.android.code_check.di

import jp.co.yumemi.android.code_check.data.remote.di.remoteModule
import jp.co.yumemi.android.code_check.data.repository.di.repositoryModule
import jp.co.yumemi.android.code_check.features.di.featureModule

val appComponent= listOf( featureModule,repositoryModule, remoteModule)