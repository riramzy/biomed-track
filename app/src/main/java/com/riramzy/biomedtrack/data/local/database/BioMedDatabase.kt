package com.riramzy.biomedtrack.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.riramzy.biomedtrack.data.local.dao.EquipmentDao
import com.riramzy.biomedtrack.data.local.dao.MaintenanceLogDao
import com.riramzy.biomedtrack.data.local.dao.StatusChangeLogDao
import com.riramzy.biomedtrack.data.local.dao.TaskDao
import com.riramzy.biomedtrack.data.local.dao.TechnicianDao
import com.riramzy.biomedtrack.data.local.entity.EquipmentEntity
import com.riramzy.biomedtrack.data.local.entity.MaintenanceLogEntity
import com.riramzy.biomedtrack.data.local.entity.StatusChangeLogEntity
import com.riramzy.biomedtrack.data.local.entity.TaskEntity
import com.riramzy.biomedtrack.data.local.entity.TechnicianEntity

@Database(
    entities = [
        EquipmentEntity::class,
        MaintenanceLogEntity::class,
        StatusChangeLogEntity::class,
        TaskEntity::class,
        TechnicianEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class BioMedDatabase: RoomDatabase() {
    abstract fun equipmentDao(): EquipmentDao
    abstract fun maintenanceLogDao(): MaintenanceLogDao
    abstract fun statusChangeLogDao(): StatusChangeLogDao
    abstract fun taskDao(): TaskDao
    abstract fun technicianDao(): TechnicianDao
}