package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.utils.UserRole

@Entity(tableName = "technician")
data class TechnicianEntity(
    @PrimaryKey val id: String,
    val name: String,
    val employeeId: String,
    val email: String,
    val role: UserRole,
    val assignedDepartments: List<Department>,
    val isActive: Boolean
) {
    fun toDomain() = Technician(
        id = id,
        name = name,
        employeeId = employeeId,
        email = email,
        role = role,
        assignedDepartments = assignedDepartments,
        isActive = isActive
    )
}

fun Technician.toEntity() = TechnicianEntity(
    id = id,
    name = name,
    employeeId = employeeId,
    email = email,
    role = role,
    assignedDepartments = assignedDepartments,
    isActive = isActive
)