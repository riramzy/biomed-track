package com.riramzy.biomedtrack.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserManagementUiState {
    object Loading : UserManagementUiState()
    data class Success(
        val users: List<Technician>,
        val adminCount: Int,
        val supervisorCount: Int,
        val technicianCount: Int,
        val totalUsersCount: Int,
    ) : UserManagementUiState()
    data class Error(val message: String) : UserManagementUiState()
}

@HiltViewModel
class UserManagementVm @Inject constructor(
    private val authRepo: AuthRepo,
    departmentRepo: DepartmentRepo,
    private val sessionManager: SessionManager
): ViewModel() {
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()
    private val _userCreatedEvent = MutableSharedFlow<Unit>()
    val userCreatedEvent = _userCreatedEvent.asSharedFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _selectedRole = MutableStateFlow("All")
    val selectedRole: StateFlow<String> = _selectedRole.asStateFlow()
    val allDepartments: StateFlow<List<Department>> = departmentRepo.getAllDepartments()
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<UserManagementUiState> = combine(
        authRepo.getAllUsers(),
        _searchQuery,
        _selectedRole
    ) { users, searchQuery, selectedRole ->
        val totalUsersCount = users.size
        val adminCount = users.count { it.role == UserRole.ADMIN }
        val supervisorCount = users.count { it.role == UserRole.SUPERVISOR }
        val technicianCount = users.count { it.role == UserRole.TECHNICIAN }

        val filteredUsers = users.filter { user ->
            val matchedQuery = user.name.contains(searchQuery, ignoreCase = true) ||
                    user.email.contains(searchQuery, ignoreCase = true)
            val matchedUsers = when (selectedRole) {
                "Admins" -> user.role == UserRole.ADMIN
                "Supervisors" -> user.role == UserRole.SUPERVISOR
                "Technicians" -> user.role == UserRole.TECHNICIAN
                else -> true
            }
            matchedQuery && matchedUsers
        }

        UserManagementUiState.Success(
            users = filteredUsers,
            adminCount = adminCount,
            supervisorCount = supervisorCount,
            technicianCount = technicianCount,
            totalUsersCount = totalUsersCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = UserManagementUiState.Loading
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedRole(role: String) {
        _selectedRole.value = role
    }

    fun toggleUserActiveStatus(user: Technician) {
        if (user.id == sessionManager.currentUser.value?.id) {
            _snackbarMessage.value = "You cannot deactivate your own account"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = if (user.isActive) {
                authRepo.deactivateUser(user.id)
            } else {
                authRepo.activateUser(user.id)
            }

            when (result) {
                is Result.Success -> _snackbarMessage.value = "User active status updated successfully"
                is Result.Error -> _snackbarMessage.value = result.message
                else -> Unit
            }
        }
    }

    fun changeUserRole(userId: String, newRole: UserRole) {
        if (userId == sessionManager.currentUser.value?.id) {
            _snackbarMessage.value = "You cannot change your own role"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            when(val result = authRepo.updateUserRole(userId, newRole)) {
               is Result.Success -> _snackbarMessage.value = "User role updated successfully"
               is Result.Error -> _snackbarMessage.value = result.message
                else -> {}
           }
        }
    }

    fun updateUserDepartments(userId: String, departments: List<Department>) {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = authRepo.updateUserDepartments(userId, departments)) {
                is Result.Success -> _snackbarMessage.value = "User departments updated successfully"
                is Result.Error -> _snackbarMessage.value = result.message
                else -> {}
            }
        }
    }

    fun createUser(user: Technician, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = authRepo.createUser(user, password)) {
                is Result.Success -> {
                    _snackbarMessage.value = "User created successfully"
                    _userCreatedEvent.emit(Unit)
                }
                is Result.Error -> _snackbarMessage.value = result.message
                else -> {}
            }
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }
}