package com.riramzy.biomedtrack.ui.screens.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.repo.AuthRepo
import com.riramzy.biomedtrack.domain.repo.DepartmentRepo
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @param:ApplicationContext val context: Context,
    private val authRepo: AuthRepo,
    departmentRepo: DepartmentRepo,
    private val sessionManager: SessionManager
): ViewModel() {
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()
    private val _isSnackbarMessageAnError = MutableStateFlow(false)
    val isSnackbarMessageError: StateFlow<Boolean> = _isSnackbarMessageAnError.asStateFlow()
    private val _userCreatedEvent = MutableSharedFlow<Unit>()
    val userCreatedEvent = _userCreatedEvent.asSharedFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _selectedRoleIndex = MutableStateFlow(0)
    val selectedRoleIndex: StateFlow<Int> = _selectedRoleIndex.asStateFlow()
    val allDepartments: StateFlow<List<Department>> = departmentRepo.getAllDepartments()
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val currentUser = sessionManager.currentUser

    val uiState: StateFlow<UserManagementUiState> = combine(
        authRepo.getAllUsers(),
        _searchQuery,
        _selectedRoleIndex
    ) { users, searchQuery, selectedRole ->
        val totalUsersCount = users.size
        val adminCount = users.count { it.role == UserRole.ADMIN }
        val supervisorCount = users.count { it.role == UserRole.SUPERVISOR }
        val technicianCount = users.count { it.role == UserRole.TECHNICIAN }

        val filteredUsers = users.filter { user ->
            val matchedQuery = user.name.contains(searchQuery, ignoreCase = true) ||
                    user.email.contains(searchQuery, ignoreCase = true)
            val matchedUsers = when (selectedRole) {
                1 -> user.role == UserRole.ADMIN
                2 -> user.role == UserRole.SUPERVISOR
                3 -> user.role == UserRole.TECHNICIAN
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

    fun setSelectedRole(index: Int) {
        _selectedRoleIndex.value = index
    }

    fun toggleUserActiveStatus(user: Technician) {
        if (user.id == sessionManager.currentUser.value?.id) {
            _isSnackbarMessageAnError.value = true
            _snackbarMessage.value = context.getString(R.string.user_mgmt_error_self_deactivate)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = if (user.isActive) {
                authRepo.deactivateUser(user.id)
            } else {
                authRepo.activateUser(user.id)
            }

            when (result) {
                is Result.Success -> {
                    _snackbarMessage.value =
                        context.getString(R.string.user_mgmt_success_status_updated)
                    _isSnackbarMessageAnError.value = false
                }
                is Result.Error -> {
                    _snackbarMessage.value = result.message
                    _isSnackbarMessageAnError.value = true
                }
                else -> Unit
            }
        }
    }

    fun changeUserRole(userId: String, newRole: UserRole) {
        if (userId == sessionManager.currentUser.value?.id) {
            _isSnackbarMessageAnError.value = true
            _snackbarMessage.value = context.getString(R.string.user_mgmt_error_self_role_change)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            when(val result = authRepo.updateUserRole(userId, newRole)) {
               is Result.Success -> {
                   _snackbarMessage.value =
                       context.getString(R.string.user_mgmt_success_role_updated)
                   _isSnackbarMessageAnError.value = false
               }
               is Result.Error -> {
                   _snackbarMessage.value = result.message
                   _isSnackbarMessageAnError.value = true
               }
                else -> {}
           }
        }
    }

    fun updateUserDepartments(userId: String, departments: List<Department>) {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = authRepo.updateUserDepartments(userId, departments)) {
                is Result.Success -> {
                    _snackbarMessage.value =
                        context.getString(R.string.user_mgmt_success_departments_updated)
                    _isSnackbarMessageAnError.value = false
                }
                is Result.Error -> {
                    _snackbarMessage.value = result.message
                    _isSnackbarMessageAnError.value = true
                }
                else -> {}
            }
        }
    }

    fun createUser(user: Technician, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = authRepo.createUser(user, password)) {
                is Result.Success -> {
                    _snackbarMessage.value =
                        context.getString(R.string.user_mgmt_success_user_created)
                    _userCreatedEvent.emit(Unit)
                    _isSnackbarMessageAnError.value = false
                }
                is Result.Error -> {
                    _snackbarMessage.value = result.message
                    _isSnackbarMessageAnError.value = true
                }
                else -> {}
            }
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }
}