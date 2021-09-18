package jp.co.yumemi.android.code_check.features.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.data.repository.GithubRepositoryImpl
import jp.co.yumemi.android.code_check.data.repository.utils.Resource

class GithubUseCase(private val repository: GithubRepositoryImpl) {

//    suspend operator fun invoke(keySearch : String): LiveData<Resource<List<Item>>> {
//        return Transformations.map(repository.searchGithub(keySearch)) {
//            it
//        }
//    }
}