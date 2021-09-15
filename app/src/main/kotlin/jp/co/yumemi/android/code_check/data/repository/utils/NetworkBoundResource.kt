package jp.co.yumemi.android.code_check.data.repository.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.coroutineContext

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): NetworkBoundResource<ResultType, RequestType> {
        withContext(Dispatchers.Main) { result.value =
            Resource.loading(null)
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            try {
                fetchFromNetwork()
            } catch (e: Exception) {
                Log.e("NetworkBoundResource", "An error happened: $e")
                setValue(Resource.error(e, null))
            }
        }
        return this
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>


    private suspend fun fetchFromNetwork() {
        Log.d(NetworkBoundResource::class.java.name, "Fetch data from network")
        setValue(Resource.loading(null)) // Dispatch latest value quickly (UX purpose)
        val apiResponse = createCallAsync()
        setValue(Resource.success(processResponse(apiResponse)))
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) result.postValue(newValue)
    }

    @WorkerThread
    protected abstract  suspend fun processResponse(response: RequestType): ResultType


    @MainThread
    protected  abstract suspend fun createCallAsync(): RequestType
}