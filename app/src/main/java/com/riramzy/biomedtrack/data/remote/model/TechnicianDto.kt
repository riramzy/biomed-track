package com.riramzy.biomedtrack.data.remote.model

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.utils.UserRole

@Keep
data class TechnicianDto(
    val id: String = "",
    val name: String = "",
    val employeeId: String = "",
    val email: String = "",
    val role: String = "",
    val assignedDepartments: List<DepartmentDto> = emptyList(),
    @get:PropertyName("isActive")
    @set:PropertyName("isActive")
    var isActive: Boolean = false
) {
    fun toDomain() = Technician(
        id = id,
        name = name,
        employeeId = employeeId,
        email = email,
        role = UserRole.valueOf(role),
        assignedDepartments = assignedDepartments.map {
            it.toDomain()
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
        it.toDto()
    },
    isActive = isActive
)