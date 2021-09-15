package jp.co.yumemi.android.code_check.data.repository

import androidx.lifecycle.LiveData
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.data.remote.GithubRemote
import jp.co.yumemi.android.code_check.data.repository.utils.NetworkBoundResource
import jp.co.yumemi.android.code_check.data.repository.utils.Resource
import org.json.JSONArray
import org.json.JSONObject

class GithubRepositoryImpl(private val githubDataSource: GithubRemote) : IGithubRepository {
    override suspend fun searchGithub(keySearch: String): LiveData<Resource<List<Item>>> {
        return object : NetworkBoundResource<List<Item>, HttpResponse>(){


            override  suspend fun createCallAsync(): HttpResponse {
                return githubDataSource.searchGithubData(url = "https://api.github.com/search/repositories") {
                    header("Accept", "application/vnd.github.v3+json")
                    parameter("q", keySearch)
                }
            }

            override suspend fun processResponse(response: HttpResponse): List<Item> {
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
                            val language = item.optString("language")?:""
                            val stargazersCount = item.optLong("stargazers_count")
                            val watchersCount = item.optLong("watchers_count")
                            val forksCount = item.optLong("forks_count")
                            val openIssuesCount = item.optLong("open_issues_count")

                            items.add(
                                Item(
                                    name = name,
                                    ownerIconUrl = ownerIconUrl,
                                    language = language,
                                    stargazersCount = stargazersCount,
                                    watchersCount = watchersCount,
                                    forksCount = forksCount,
                                    openIssuesCount = openIssuesCount
                                )
                            )
                        }
                    }
                }
                return  items
            }
        }.build().asLiveData()

    }

}