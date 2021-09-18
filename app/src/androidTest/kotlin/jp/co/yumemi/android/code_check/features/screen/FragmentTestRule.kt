package jp.co.yumemi.android.code_check.features.screen

import androidx.fragment.app.Fragment
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.main.MainActivity

abstract class FragmentTestRule<F: Fragment>: ActivityTestRule<MainActivity>(MainActivity::class.java, true) {
    override fun afterActivityLaunched() {
        super.afterActivityLaunched()
        activity.runOnUiThread {
            val fm = activity.supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, createFragment())
                .commit()
        }
    }

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        val application = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext
    }
    protected abstract fun createFragment(): F
}

fun <F: Fragment> createRule(fragment: F): FragmentTestRule<F> =
    object: FragmentTestRule<F>() {
        override fun createFragment(): F = fragment

    }