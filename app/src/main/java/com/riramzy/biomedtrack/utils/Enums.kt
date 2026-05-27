package com.riramzy.biomedtrack.utils

enum class ActivityType {
    STATUS_CHANGE,
    MAINTENANCE_LOG,
    TASK_ASSIGNED
}

enum class EquipmentStatus {
    ONLINE,
    SERVICE,
    DOWN
}

enum class UserRole {
    ADMIN,
    TECHNICIAN,
    SUPERVISOR
}

enum class MaintenanceType {
    ROUTINE,
    REPAIR,
    INSPECTION,
    CALIBRATION,
    PREVENTIVE,
    CORRECTIVE,
    EXTERNAL
}

enum class TaskStatus {
    PENDING,
    IN_PROGRESS,
    DONE
}