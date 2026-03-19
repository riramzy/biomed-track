package com.riramzy.biomedtrack.data.remote.model

import androidx.annotation.Keep
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.model.UserRole

@Keep
data class TechnicianDto(
    val id: String = "",
    val name: String = "",
    val employeeId: String = "",
    val email: String = "",
    val role: String = "",
    val assignedDepartments: List<String> = emptyList(),
    val isActive: Boolean = false
) {
    fun toDomain() = Technician(
        id = id,
        name = name,
        employeeId = employeeId,
        email = email,
        role = UserRole.valueOf(role),
        assignedDepartments = assignedDepartments.map {
            Department.valueOf(it)
        },
        isActive = isActive
    )
}

fun Technician.toDto() = TechnicianDto(
    id = id,
    name = name,
    employeeId = employeeId,
    email = email,
    role = role.name,
    assignedDepartments = assignedDepartments.map {
        it.name
    },
    isActive = isActive
)