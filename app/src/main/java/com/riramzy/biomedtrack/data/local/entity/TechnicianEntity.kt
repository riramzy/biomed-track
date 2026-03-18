package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.domain.model.UserRole

@Entity(tableName = "technician")
data class TechnicianEntity(
    @PrimaryKey val id: String,
    val name: String,
    val employeeId: String,
    val email: String,
    val role: String,
    val assignedDepartments: String,
    val isActive: Boolean
) {
    fun toDomain() = Technician(
        id = id,
        name = name,
        employeeId = employeeId,
        email = email,
        role = UserRole.valueOf(role),
        assignedDepartments = Gson().fromJson(assignedDepartments, Array<Department>::class.java).toList(),
        isActive = isActive
    )
}

fun Technician.toEntity() = TechnicianEntity(
    id = id,
    name = name,
    employeeId = employeeId,
    email = email,
    role = role.name,
    assignedDepartments = Gson().toJson(assignedDepartments),
    isActive = isActive
)