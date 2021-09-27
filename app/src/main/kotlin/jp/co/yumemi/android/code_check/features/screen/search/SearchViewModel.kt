package jp.co.yumemi.android.code_check.features.screen.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.data.repository.GithubRepositoryImpl
import jp.co.yumemi.android.code_check.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * UI state for the Search screen
 */
data class SearchUiState(
    val searchText: String = "",
    val items: List<Item> = emptyList(),
    val loading: Boolean = false,
    val isSearchError: Boolean = false
)

class SearchViewModel(private val repository: GithubRepositoryImpl) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    fun onQueryText(string: String) {
        _uiState.update {
            it.copy(searchText = string)
        }
        searchText = string
    }

    var page = 1
    var perPage = 6
    var searchText = ""
    fun searchOnGithub(isRefresh: Boolean = true) {
        if (isRefresh) {
            page = 1
        } else {
            page += 1
        }
        _uiState.update { it.copy(loading = true, isSearchError = false) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res =
                    repository.searchGithub(keySearch = searchText, page = page, perPage = perPage)
                if (!res.isNullOrEmpty()){
                    val result = if (isRefresh) {
                        res
                    } else {
                        val data = res.toMutableList()
                        data.addAll(0,_uiState.value.items)
                        data
                    }
                    withContext(Dispatchers.Main) {
                        MainActivity.lastSearchDate = Date()
                            _uiState.update {
                            it.copy(
                                items =result, loading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(isSearchError = true, loading = false)
                    }
                }
            }

        }
    }
}