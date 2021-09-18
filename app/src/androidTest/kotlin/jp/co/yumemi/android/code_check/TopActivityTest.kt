package jp.co.yumemi.android.code_check

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class TopActivityTest{
    @get:Rule
    val activityRule  = ActivityScenarioRule(TopActivity::class.java)
    @Test
    fun testActivityInView(){
        // TODO fix bugs in MUI xiaomi note 8
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
}