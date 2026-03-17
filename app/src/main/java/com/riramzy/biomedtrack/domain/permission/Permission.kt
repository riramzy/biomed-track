package com.riramzy.biomedtrack.domain.permission

import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.UserRole

val rolePermission = mapOf(
    UserRole.ADMIN to Permission.entries.toSet(),

    UserRole.TECHNICIAN to setOf(
        Permission.VIEW_EQUIPMENT,
        Permission.VIEW_OWN_LOGS,
        Permission.ADD_MAINTENANCE_LOG,
        Permission.VIEW_SCHEDULER
    ),

    UserRole.SUPERVISOR to setOf(
        Permission.VIEW_EQUIPMENT,
        Permission.ADD_EQUIPMENT,
        Permission.EDIT_EQUIPMENT,
        Permission.DELETE_EQUIPMENT,
        Permission.VIEW_OWN_LOGS,
        Permission.VIEW_ALL_LOGS,
        Permission.ADD_MAINTENANCE_LOG,
        Permission.DELETE_LOG,
        Permission.VIEW_REPORTS,
        Permission.GENERATE_REPORTS,
        Permission.VIEW_SCHEDULER,
        Permission.ASSIGN_TASKS,
    )
)

enum class Permission {
    VIEW_EQUIPMENT,
    ADD_EQUIPMENT,
    EDIT_EQUIPMENT,
    DELETE_EQUIPMENT,
    VIEW_OWN_LOGS,
    VIEW_ALL_LOGS,
    ADD_MAINTENANCE_LOG,
    DELETE_LOG,
    VIEW_REPORTS,
    GENERATE_REPORTS,
    MANAGE_USERS,
    VIEW_SCHEDULER,
    ASSIGN_TASKS,
    IMPORT_EQUIPMENT
}

fun UserRole.hasPermission(permission: Permission): Boolean {
    return rolePermission[this]?.contains(permission) ?: false
}

fun UserRole.canWriteToDepartment(department: Department, assignedDepartments: List<Department>): Boolean {
    if (this == UserRole.ADMIN || this == UserRole.SUPERVISOR) return true
    return assignedDepartments.contains(department)
}