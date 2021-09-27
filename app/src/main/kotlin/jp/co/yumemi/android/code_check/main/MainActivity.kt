/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.annotation.ExperimentalCoilApi
import java.security.MessageDigest
import java.util.*

class MainActivity : AppCompatActivity() {
    private fun GetKeyHash() {
        try {
            val info = packageManager.getPackageInfo(
                applicationContext.packageName, PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashkey_value = String(Base64.encode(md.digest(), 0))
                Log.e("HashKey", hashkey_value)
                //check you logcat hash key value
            }
        } catch (e: java.lang.Exception) {
//            Log.e("exception", e.toString());
        }
    }
    @ExperimentalAnimationApi
    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        GetKeyHash()
        setIconStatusBarColor()
       setContent {
           YumemiApp()
       }
    }
    private  fun setIconStatusBarColor(){
        val window = window
        window.statusBarColor = android.graphics.Color.WHITE
        getWindow().getDecorView().systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

    }
    companion object {
        lateinit var lastSearchDate: Date
    }
}
