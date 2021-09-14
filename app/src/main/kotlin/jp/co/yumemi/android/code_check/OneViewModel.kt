/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * TwoFragment で使う
 */
class OneViewModel(
    private val context: Context
) : ViewModel() {

    // 検索結果
    fun searchResults(inputText: String): List<Item> = runBlocking {
        val client = HttpClient(Android)
        /**
         * leak memory with GlobalScope
         */
        return@runBlocking GlobalScope.async {
            val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", inputText)
            }

            val jsonBody = JSONObject(response.receive<String>())

            val jsonItems : JSONArray? = jsonBody.optJSONArray("items")

            val items = mutableListOf<Item>()

            /**
             * アイテムの個数分ループする
             */
            jsonItems?.let { jsonArray ->
                for (i in 0 until jsonArray.length()) {
                    val jsonItem :JSONObject?= jsonArray.optJSONObject(i)
                    jsonItem?.let {item->
                        val name = item.optString("full_name")
                        val ownerIconUrl = item.optJSONObject("owner")?.optString("avatar_url")?:""
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


            lastSearchDate = Date()

            return@async items.toList()
        }.await()
    }
}

@Parcelize
data class Item(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable