package com.riramzy.biomedtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.ui.screens.equipment.add.AddEquipmentAction
import com.riramzy.biomedtrack.utils.EquipmentStatus

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val model: String,
    val serialNumber: String,
    val manufacturer: String,
    val agent: String,
    val category: String,
    val department: Department,
    val status: EquipmentStatus,
    val location: String,
    val installDate: Long,
    val lastMaintenanceDate: Long? = null,
    val nextMaintenanceDate: Long? = null,
    val serviceIntervalDays: Int,
    val contractInfo: String? = null,
    val warrantyEndDate: Long? = null,
    val createdBy: String,
    val assignedTo: String? = null,
) {
    fun toDomain() = Equipment(
        id = id,
        name = name,
        model = model,
        serialNumber = serialNumber,
        manufacturer = manufacturer,
        agent = agent,
        category = category,
        department = department,
        status = status,
        location = location,
        installDate = installDate,
        lastMaintenanceDate = lastMaintenanceDate,
        nextMaintenanceDate = nextMaintenanceDate,
        serviceIntervalDays = serviceIntervalDays,
        contractInfo = contractInfo,
        warrantyEndDate = warrantyEndDate,
        createdBy = createdBy,
        assignedTo = assignedTo,
    )
}

fun Equipment.toEntity() = EquipmentEntity(
    id = id,
    name = name,
    model = model,
    serialNumber = serialNumber,
    manufacturer = manufacturer,
    agent = agent,
    category = category,
    department = department,
    status = status,
    location = location,
    installDate = installDate,
    lastMaintenanceDate = lastMaintenanceDate,
    nextMaintenanceDate = nextMaintenanceDate,
    serviceIntervalDays = serviceIntervalDays,
    contractInfo = contractInfo,
    warrantyEndDate = warrantyEndDate,
    createdBy = createdBy,
    assignedTo = assignedTo,
)