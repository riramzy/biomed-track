package com.riramzy.biomedtrack

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.riramzy.biomedtrack.ui.screens.admin.UserManagementScreen
import com.riramzy.biomedtrack.ui.screens.auth.LoginScreen
import com.riramzy.biomedtrack.ui.screens.auth.SplashScreen
import com.riramzy.biomedtrack.ui.screens.dashboard.DashboardScreen
import com.riramzy.biomedtrack.ui.screens.equipment.add.AddEquipmentScreen
import com.riramzy.biomedtrack.ui.screens.equipment.edit.EditEquipmentScreen
import com.riramzy.biomedtrack.ui.screens.equipment.details.EquipmentDetailScreen
import com.riramzy.biomedtrack.ui.screens.importing.ImportEquipmentSelectFileScreen
import com.riramzy.biomedtrack.ui.screens.inventory.InventoryScreen
import com.riramzy.biomedtrack.ui.screens.maintenance.LogMaintenanceScreen
import com.riramzy.biomedtrack.ui.screens.notifications.NotificationsScreen
import com.riramzy.biomedtrack.ui.screens.reports.ReportsScreen
import com.riramzy.biomedtrack.ui.screens.scheduler.SchedulerScreen
import com.riramzy.biomedtrack.utils.Screen

@Composable
fun BioMedApp(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }
        composable(Screen.Inventory.route) {
            InventoryScreen(navController)
        }
        composable(Screen.Scheduler.route) {
            SchedulerScreen(navController)
        }
        composable(Screen.Reports.route) {
            ReportsScreen(navController)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.UserManagement.route) {
            UserManagementScreen(navController)
        }
        composable(Screen.ImportEquipment.route) {
            ImportEquipmentSelectFileScreen(navController)
        }

        composable(
            Screen.EquipmentDetail.route,
            arguments = listOf(navArgument("equipmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val equipmentId = backStackEntry.arguments?.getString("equipmentId")
            EquipmentDetailScreen(navController = navController)
        }

        composable(
            Screen.LogMaintenance.route,
            arguments = listOf(navArgument("equipmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val equipmentId = backStackEntry.arguments?.getString("equipmentId")
            LogMaintenanceScreen(navController)
        }

        composable(
            Screen.EditEquipment.route,
            arguments = listOf(navArgument("equipmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val equipmentId = backStackEntry.arguments?.getString("equipmentId")
            EditEquipmentScreen(navController)
        }

        composable(Screen.AddEquipment.route) {
            AddEquipmentScreen(navController)
        }
    }
}