package jp.co.yumemi.android.code_check

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import jp.co.yumemi.android.code_check.di.appComponent
import jp.co.yumemi.android.code_check.features.LoginManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class YumemiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        LoginManager.isLogin.value = FirebaseAuth.getInstance().currentUser!=null
        startKoin {
            androidContext(this@YumemiApp)
            modules(appComponent)
        }
    }
}