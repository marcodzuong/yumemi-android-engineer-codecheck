package jp.co.yumemi.android.code_check.features.screen.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.data.repository.GithubRepositoryImpl
import jp.co.yumemi.android.code_check.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SearchViewModel(private val repository: GithubRepositoryImpl) : ViewModel() {

    val searchList = mutableStateOf<List<Item>>(listOf())
    val isLoading = mutableStateOf(false)
    val isSearchError = mutableStateOf(false)
    fun searchOnGithub(search: String) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = repository.searchGithub(search)
                withContext(Dispatchers.Main) {
                    MainActivity.lastSearchDate = Date()
                    searchList.value = res
                    isLoading.value = false
                }
            } catch (e: Exception) {
                isSearchError.value = true
                isLoading.value= false
            }

        }
    }
}