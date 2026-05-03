package com.riramzy.biomedtrack.data.local.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.MaintenanceType
import com.riramzy.biomedtrack.utils.TaskStatus
import com.riramzy.biomedtrack.utils.UserRole

@ProvidedTypeConverter
class RoomConverters(private val gson: Gson) {
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
        return gson.toJson(department)
    }

    @TypeConverter
    fun toDepartment(departmentString: String): Department {
        return gson.fromJson(departmentString, Department::class.java)
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
        return gson.toJson(itemList)
    }

    @TypeConverter
    fun toChecklistItemsList(itemListString: String): List<ChecklistItem> {
        return gson.fromJson(itemListString, Array<ChecklistItem>::class.java).toList()
    }

    @TypeConverter
    fun fromDepartmentsList(departments: List<Department>): String {
        return gson.toJson(departments)
    }

    @TypeConverter
    fun toDepartmentsList(departmentsString: String): List<Department> {
        return gson.fromJson(departmentsString, Array<Department>::class.java).toList()
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(listString: String): List<String> {
        return gson.fromJson(listString, Array<String>::class.java).toList()
    }
}