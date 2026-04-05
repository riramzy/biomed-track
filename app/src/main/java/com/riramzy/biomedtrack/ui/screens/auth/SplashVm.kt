package com.riramzy.biomedtrack.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashNavEvent {
    object NavigateToMain: SplashNavEvent()
    object NavigateToLogin: SplashNavEvent()

}

@HiltViewModel
class SplashVm @Inject constructor(
    authRepo: AuthRepo,
    sessionManager: SessionManager
): ViewModel() {
    private val _navigationEvent = MutableStateFlow<SplashNavEvent?>(null)
    val navigationEvent: StateFlow<SplashNavEvent?> = _navigationEvent.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1500)
            val user = authRepo.getCurrentUser()
            if (user != null) {
                sessionManager.setUser(user)
                _navigationEvent.value = SplashNavEvent.NavigateToMain
            } else {
                _navigationEvent.value = SplashNavEvent.NavigateToLogin
            }
        }
    }
}