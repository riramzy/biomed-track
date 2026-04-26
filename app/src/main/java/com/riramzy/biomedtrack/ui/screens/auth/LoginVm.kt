package com.riramzy.biomedtrack.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    data object Idle: LoginUiState()
    data object Loading: LoginUiState()
    data class Success(val technician: Technician): LoginUiState()
    data class Error(val message: String): LoginUiState()
}

@HiltViewModel
class LoginVm @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(
        email: String,
        password: String
    ) {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            val result = loginUseCase(email, password)
            _uiState.value = when (result) {
                is Result.Success -> LoginUiState.Success(result.data)
                is Result.Error -> LoginUiState.Error(result.message)
                else -> LoginUiState.Error("Unknown error")
            }
        }
    }
}