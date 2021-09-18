package jp.co.yumemi.android.code_check.features.screen.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import io.ktor.client.engine.android.*
import jp.co.yumemi.android.code_check.data.remote.GithubRemote
import jp.co.yumemi.android.code_check.data.repository.GithubRepositoryImpl
import jp.co.yumemi.android.code_check.features.domain.GithubUseCase
import jp.co.yumemi.android.code_check.getOrAwaitData
import junit.framework.TestCase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class OneViewModelTest  {
    private lateinit var viewModel : OneViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Test
    fun testViewModel(){
        val githubRemote  = GithubRemote()
        val githubRepository  = GithubRepositoryImpl(githubRemote)
        val githubUseCase  = GithubUseCase(githubRepository)
        viewModel = OneViewModel(githubUseCase)
        viewModel.searchResults("kotlin")
        val result = viewModel.search.getOrAwaitData().isNotEmpty()
        assert(result)
    }
}