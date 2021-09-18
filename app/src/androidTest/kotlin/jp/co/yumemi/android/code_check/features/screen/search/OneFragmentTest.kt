package jp.co.yumemi.android.code_check.features.screen.search

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.services.events.TestEventException
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.data.remote.GithubRemote
import jp.co.yumemi.android.code_check.data.repository.GithubRepositoryImpl
import jp.co.yumemi.android.code_check.features.domain.GithubUseCase
import jp.co.yumemi.android.code_check.features.screen.createRule
import jp.co.yumemi.android.code_check.features.screen.search.adapter.CustomAdapter
import jp.co.yumemi.android.code_check.getOrAwaitData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4ClassRunner::class)
class OneFragmentTest {

    private val fragment: SearchFragment = SearchFragment()

    @get:Rule
    val fragmentRule = createRule(fragment)

    @Test
    fun testFragmentIsVisibleOnAppLaunch() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun testTextBox(){
//        onView(withId(R.id.searchInputText)).perform(typeText("kotlin"))
//        closeSoftKeyboard()
//        val githubRemote  = GithubRemote()
//        val githubRepository  = GithubRepositoryImpl(githubRemote)
//        val githubUseCase  = GithubUseCase(githubRepository)
//        val viewModel = OneViewModel(githubUseCase)
//        viewModel.searchResults("kotlin")
//        val result = viewModel.search.getOrAwaitData()
//        assert(result[0].name.contains("kotlin"))
    }
    @Test
    @Throws(InterruptedException::class)
    fun testCaseForRecyclerClick() {
//        Thread.sleep(1000)
//        InstrumentationRegistry.getInstrumentation().runOnMainSync(Runnable {
//            fragment.mAdapter.submitList(arrayListOf<Item>().apply {
//                for (i in 0 until 10) {
//                    add(
//                        Item(
//                            id = i.toLong(),
//                            name = "name + $i",
//                            ownerIconUrl = "ownerIconUrl",
//                            language = "language",
//                            stargazersCount = 10,
//                            watchersCount = 10,
//                            forksCount = 10,
//                            openIssuesCount = 10
//                        )
//                    )
//                }
//            })
//            onView(withId(R.id.recyclerView)).perform(
//                RecyclerViewActions.actionOnItemAtPosition<CustomAdapter.ViewHolder>(
//                    0,
//                    click()
//                )
//            )
//        })

    }

    @Test
    @Throws(InterruptedException::class)
    fun testCaseForRecyclerScroll() {
        Thread.sleep(1000)
        val recyclerView =
            fragment.view?.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView != null) {
            val itemCount =
                Objects.requireNonNull(recyclerView.adapter!!).itemCount
            onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                    itemCount - 1
                )
            )
        }

    }
}