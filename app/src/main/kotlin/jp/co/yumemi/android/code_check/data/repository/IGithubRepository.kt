package jp.co.yumemi.android.code_check.data.repository

import androidx.lifecycle.LiveData
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.data.repository.utils.Resource

interface IGithubRepository {
    suspend fun searchGithub(keySearch : String): LiveData<Resource<List<Item>>>
}