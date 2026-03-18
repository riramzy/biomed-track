package com.riramzy.biomedtrack.data.local.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.MaintenanceType
import com.riramzy.biomedtrack.domain.model.TaskStatus
import com.riramzy.biomedtrack.domain.model.UserRole

@ProvidedTypeConverter
class RoomConverters {
    @TypeConverter
    fun fromEquipmentStatus(status: EquipmentStatus): String {
        return status.name
    }

    @TypeConverter
    fun toEquipmentStatus(status: String): EquipmentStatus {
        return EquipmentStatus.valueOf(status)
    }

    @TypeConverter
    fun fromDepartment(department: Department): String {
        return department.name
    }

    @TypeConverter
    fun toDepartment(department: String): Department {
        return Department.valueOf(department)
    }

    @TypeConverter
    fun fromUserRole(role: UserRole): String {
        return role.name
    }

    @TypeConverter
    fun toUserRole(role: String): UserRole {
        return UserRole.valueOf(role)
    }

    @TypeConverter
    fun fromMaintenanceType(type: MaintenanceType): String {
        return type.name
    }

    @TypeConverter
    fun toMaintenanceType(type: String): MaintenanceType {
        return MaintenanceType.valueOf(type)
    }

    @TypeConverter
    fun fromTaskStatus(status: TaskStatus): String {
        return status.name
    }

    @TypeConverter
    fun toTaskStatus(status: String): TaskStatus {
        return TaskStatus.valueOf(status)
    }

    @TypeConverter
    fun fromChecklistItemsList(itemList: List<ChecklistItem>): String {
        return Gson().toJson(itemList)
    }

    @TypeConverter
    fun toChecklistItemsList(itemListString: String): List<ChecklistItem> {
        return Gson().fromJson(itemListString, Array<ChecklistItem>::class.java).toList()
    }

    @TypeConverter
    fun fromDepartmentsList(departments: List<Department>): String {
        return Gson().toJson(departments)
    }

    @TypeConverter
    fun toDepartmentsList(departmentsString: String): List<Department> {
        return Gson().fromJson(departmentsString, Array<Department>::class.java).toList()
    }
}