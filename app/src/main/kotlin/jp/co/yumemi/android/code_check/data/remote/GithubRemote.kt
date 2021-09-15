package jp.co.yumemi.android.code_check.data.remote

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class GithubRemote {
    private val httpClient: HttpClient = HttpClient(Android)
    suspend fun searchGithubData(
        url: String,
        request: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        return httpClient.get(url) {
            request()
        }
    }

}