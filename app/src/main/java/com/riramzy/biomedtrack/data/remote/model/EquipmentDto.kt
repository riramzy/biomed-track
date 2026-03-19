package com.riramzy.biomedtrack.data.remote.model

import androidx.annotation.Keep
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.EquipmentStatus

@Keep
data class EquipmentDto(
    val id: String = "",
    val name: String = "",
    val model: String = "",
    val serialNumber: String = "",
    val manufacturer: String = "",
    val agent: String = "",
    val category: String = "",
    val department: String = "",
    val status: String = "",
    val location: String = "",
    val installDate: String = "",
    val lastMaintenanceDate: String? = null,
    val nextMaintenanceDate: String? = null,
    val serviceIntervalDays: Int = 90,
    val contractInfo: String? = null,
    val createdBy: String = "",
    val assignedTo: String? = null
) {
    fun toDomain() = Equipment(
        id = id,
        name = name,
        model = model,
        serialNumber = serialNumber,
        manufacturer = manufacturer,
        agent = agent,
        category = category,
        department = Department.valueOf(department),
        status = EquipmentStatus.valueOf(status),
        location = location,
        installDate = installDate,
        lastMaintenanceDate = lastMaintenanceDate,
        nextMaintenanceDate = nextMaintenanceDate,
        serviceIntervalDays = serviceIntervalDays,
        contractInfo = contractInfo,
        createdBy = createdBy,
        assignedTo = assignedTo,
    )
}

fun Equipment.toDto() = EquipmentDto(
    id = id,
    name = name,
    model = model,
    serialNumber = serialNumber,
    manufacturer = manufacturer,
    agent = agent,
    category = category,
    department = department.name,
    status = status.name,
    location = location,
    installDate = installDate,
    lastMaintenanceDate = lastMaintenanceDate,
    nextMaintenanceDate = nextMaintenanceDate,
    serviceIntervalDays = serviceIntervalDays,
    contractInfo = contractInfo,
    createdBy = createdBy,
    assignedTo = assignedTo,
)