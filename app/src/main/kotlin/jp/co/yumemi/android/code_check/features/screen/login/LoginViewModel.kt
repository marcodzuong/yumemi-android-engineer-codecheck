package jp.co.yumemi.android.code_check.features.screen.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val isGoogleLoading :Boolean = false,
    val isFacebookLoading :Boolean = false
)
class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()
    fun updateLoadingGoogle(isLoading :Boolean){
        _uiState.update {
            it.copy(isGoogleLoading = isLoading)
        }
    }

    fun updateLoadingFacebook(isLoading :Boolean){
        _uiState.update {
            it.copy(isFacebookLoading = isLoading)
        }
    }

}