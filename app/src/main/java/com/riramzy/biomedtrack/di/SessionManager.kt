package com.riramzy.biomedtrack.di

import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.permission.canWriteToDepartment
import com.riramzy.biomedtrack.domain.permission.hasPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/*
SessionManager is the single source of truth for "who is currently logged in" across the entire app.
It sits in memory for the entire lifetime of the app and every class that needs to know about
the current user asks SessionManager instead of going to Firebase or Room.
 */
@Singleton
class SessionManager @Inject constructor() {
    private val _currentUser = MutableStateFlow<Technician?>(null)
    val currentUser: StateFlow<Technician?> = _currentUser.asStateFlow()

    fun setUser(technician: Technician) {
        _currentUser.value = technician
    }

    fun clearUser() {
        _currentUser.value = null
    }

    fun hasPermission(permission: Permission): Boolean {
        return currentUser.value?.role?.hasPermission(permission) ?: false
    }

    fun canWriteToDepartment(department: Department): Boolean {
        val user = currentUser.value ?: return false
        return currentUser.value?.role?.canWriteToDepartment(department, user.assignedDepartments) ?: false
    }
}