package com.riramzy.biomedtrack.domain.model

// Represents a user of the app
data class Technician(
    val id: String,
    val name: String,
    val employeeId: String,
    val email: String,
    val role: UserRole,
    val assignedDepartments: List<Department>,
    val isActive: Boolean
)
