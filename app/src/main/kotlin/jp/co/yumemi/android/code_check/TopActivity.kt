/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class TopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
    }
    companion object {
        lateinit var lastSearchDate: Date
    }
}
