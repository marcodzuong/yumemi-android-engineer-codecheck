package jp.co.yumemi.android.code_check

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitData(): T{
    var data: T?=null
    val latch = CountDownLatch(1)
    val observer = object  : Observer<T>{
        override fun onChanged(t: T) {
            data  = t
            this@getOrAwaitData.removeObserver(this)
            latch.countDown()
        }

    }
    GlobalScope.launch(Dispatchers.Main) {
        // your code here...
        this@getOrAwaitData.observeForever(observer)
    }

    try {
        if (!latch.await(5,TimeUnit.SECONDS)){
            throw  TimeoutException("Live data never git it")
        }
    } finally {
        GlobalScope.launch(Dispatchers.Main) {
            // your code here...
            this@getOrAwaitData.removeObserver(observer)
        }
    }
    return data as T
}