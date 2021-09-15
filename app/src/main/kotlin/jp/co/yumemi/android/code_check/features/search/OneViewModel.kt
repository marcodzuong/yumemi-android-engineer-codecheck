/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.features.search

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.data.model.Item
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * TwoFragment で使う
 */
class OneViewModel(private val app: Application) : ViewModel() {
    private val context: Context by lazy {
        app.applicationContext
    }

    // 検索結果
    val searchResult: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>()
    }
    val searchError: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private val handleError = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
        searchError.postValue(true)
    }

    fun searchResults(inputText: String) {
        viewModelScope.launch(Dispatchers.IO + handleError) {
            val client = HttpClient(Android)
            val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", inputText)
            }

            val jsonBody = JSONObject(response.receive<String>())

            val jsonItems: JSONArray? = jsonBody.optJSONArray("items")

            val items = mutableListOf<Item>()

            /**
             * アイテムの個数分ループする
             */
            jsonItems?.let { jsonArray ->
                for (i in 0 until jsonArray.length()) {
                    val jsonItem: JSONObject? = jsonArray.optJSONObject(i)
                    jsonItem?.let { item ->
                        val name = item.optString("full_name")
                        val ownerIconUrl =
                            item.optJSONObject("owner")?.optString("avatar_url") ?: ""
                        val language = item.optString("language")
                        val stargazersCount = item.optLong("stargazers_count")
                        val watchersCount = item.optLong("watchers_count")
                        val forksCount = item.optLong("forks_count")
                        val openIssuesCount = item.optLong("open_issues_count")

                        items.add(
                            Item(
                                name = name,
                                ownerIconUrl = ownerIconUrl,
                                language = context.getString(R.string.written_language, language),
                                stargazersCount = stargazersCount,
                                watchersCount = watchersCount,
                                forksCount = forksCount,
                                openIssuesCount = openIssuesCount
                            )
                        )
                    }
                }
            }
            withContext(Dispatchers.Main) {
                lastSearchDate = Date()
                searchResult.value = items
            }
        }

    }
}

