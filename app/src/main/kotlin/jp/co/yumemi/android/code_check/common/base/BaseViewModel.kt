package jp.co.yumemi.android.code_check.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import jp.co.yumemi.android.code_check.navigation.NavigationCommand

open class BaseViewModel : ViewModel() {
    private val _navigation = MutableLiveData<NavigationCommand>()
    val navigation: LiveData<NavigationCommand> = _navigation
    fun navigate(directions: NavDirections) {
        _navigation.value = NavigationCommand.To(directions)
    }
}