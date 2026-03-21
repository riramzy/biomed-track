package com.riramzy.biomedtrack.ui.navigation

sealed class Screen(val route: String) {
    object Splash: Screen("splash")
    object Dashboard: Screen("dashboard")
    object Inventory: Screen("inventory")
    object Scheduler: Screen("scheduler")
    object Reports: Screen("reports")
    object Notifications: Screen("notifications")
    object Login: Screen("login")
    object UserManagement: Screen("user_management")
    object ImportEquipment: Screen("import_equipment")

    object EquipmentDetail: Screen("equipment_detail/{equipmentId}") {
        fun createRoute(equipmentId: String) = "equipment_detail/$equipmentId"
    }

    object LogMaintenance: Screen("log_maintenance/{equipmentId}") {
        fun createRoute(equipmentId: String) = "log_maintenance/$equipmentId"
    }

    object AddEquipment: Screen("add_equipment")

    object EditEquipment: Screen("edit_equipment/{equipmentId}") {
        fun createRoute(equipmentId: String) = "edit_equipment/$equipmentId"
    }
}
