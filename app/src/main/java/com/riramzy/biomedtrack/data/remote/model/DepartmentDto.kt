package com.riramzy.biomedtrack.data.remote.model

import androidx.annotation.Keep
import androidx.room.PrimaryKey
import com.riramzy.biomedtrack.domain.model.Department

@Keep
data class DepartmentDto(
    val id: String = "",
    val name: String = "",
    val totalEquipment: Int = 0,
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

fun Department.toDto() = DepartmentDto(
    id = id,
    name = name,
    totalEquipment = totalEquipment,
    dueServiceEquipment = dueServiceEquipment,
    downEquipment = downEquipment
)