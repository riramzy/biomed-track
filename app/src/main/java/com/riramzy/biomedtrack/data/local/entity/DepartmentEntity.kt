package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riramzy.biomedtrack.domain.model.Department

@Entity(tableName = "department")
data class DepartmentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val totalEquipment: Int,
    val dueServiceEquipment: Int? = 0,
    val downEquipment: Int? = 0
) {
    fun toDomain() = Department(
        id = id,
        name = name,
        totalEquipment = totalEquipment,
        dueServiceEquipment = dueServiceEquipment,
        downEquipment = downEquipment
    )
}

fun Department.toEntity() = DepartmentEntity(
    id = id,
    name = name,
    totalEquipment = totalEquipment,
    dueServiceEquipment = dueServiceEquipment,
    downEquipment = downEquipment
)