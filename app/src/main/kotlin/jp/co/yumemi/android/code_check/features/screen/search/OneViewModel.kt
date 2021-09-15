/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.features.screen.search

import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import jp.co.yumemi.android.code_check.TopActivity
import jp.co.yumemi.android.code_check.common.base.BaseViewModel
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.data.repository.utils.Resource
import jp.co.yumemi.android.code_check.features.domain.GithubUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * TwoFragment で使う
 */
class OneViewModel(private val githubUseCase: GithubUseCase) : BaseViewModel() {


    // 検索結果
  private  var searchResult: LiveData<Resource<List<Item>>> = MutableLiveData()
    val searchError: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private val handleError = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
        searchError.postValue(true)
    }
    private val _search = MediatorLiveData<List<Item>>()
    val search: LiveData<List<Item>> get() = _search
    private val _isLoading = MutableLiveData<Resource.Status>()
    val isLoading: LiveData<Resource.Status> get() = _isLoading
    fun searchResults(inputText: String) {
        viewModelScope.launch(Dispatchers.IO + handleError) {
            searchResult = githubUseCase(keySearch = inputText)
            withContext(Dispatchers.Main) {
                TopActivity.lastSearchDate = Date()
                _search.addSource(searchResult) {
                    it.data?.let {list->
                        _search.value = list
                    }
                    _isLoading.value = it.status
                    if (it.status == Resource.Status.ERROR) searchError.value = true
                }

            }

        }
    }
    fun gotoRepositoryFragment(item: Item){
//        findNavController().navigate(
//            OneFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(item))
    }

}

