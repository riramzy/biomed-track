package com.riramzy.biomedtrack.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.usecase.auth.ChangePasswordUseCase
import com.riramzy.biomedtrack.domain.usecase.auth.LoginUseCase
import com.riramzy.biomedtrack.domain.usecase.auth.LogoutUseCase
import com.riramzy.biomedtrack.utils.Result
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
class AuthVm @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _isPasswordUpdating = MutableStateFlow(false)
    val isPasswordUpdating: StateFlow<Boolean> = _isPasswordUpdating.asStateFlow()

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

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = logoutUseCase()
            if (result is Result.Success) onSuccess()
        }
    }

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        onSuccess: (Result<Unit>) -> Unit
    ) {
        _isPasswordUpdating.value = true

        viewModelScope.launch {
            val result = changePasswordUseCase(oldPassword, newPassword)
            _isPasswordUpdating.value = false
            onSuccess(result)
        }
    }
}