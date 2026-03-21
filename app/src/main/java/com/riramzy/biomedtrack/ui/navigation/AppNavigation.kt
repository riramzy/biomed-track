package com.riramzy.biomedtrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.riramzy.biomedtrack.ui.screens.admin.ImportEquipmentScreen
import com.riramzy.biomedtrack.ui.screens.admin.UserManagementScreen
import com.riramzy.biomedtrack.ui.screens.auth.LoginScreen
import com.riramzy.biomedtrack.ui.screens.auth.SplashScreen
import com.riramzy.biomedtrack.ui.screens.dashboard.DashboardScreen
import com.riramzy.biomedtrack.ui.screens.equipment.AddEquipmentScreen
import com.riramzy.biomedtrack.ui.screens.equipment.EditEquipmentScreen
import com.riramzy.biomedtrack.ui.screens.equipment.EquipmentDetailScreen
import com.riramzy.biomedtrack.ui.screens.inventory.InventoryScreen
import com.riramzy.biomedtrack.ui.screens.maintenance.LogMaintenanceScreen
import com.riramzy.biomedtrack.ui.screens.notifications.NotificationsScreen
import com.riramzy.biomedtrack.ui.screens.reports.ReportsScreen
import com.riramzy.biomedtrack.ui.screens.scheduler.SchedulerScreen

@Composable
fun AppNavigation(
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
            ImportEquipmentScreen(navController)
        }

        composable(
            Screen.EquipmentDetail.route,
            arguments = listOf(navArgument("equipmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val equipmentId = backStackEntry.arguments?.getString("equipmentId")
            EquipmentDetailScreen(navController, equipmentId)
        }

        composable(
            Screen.LogMaintenance.route,
            arguments = listOf(navArgument("equipmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val equipmentId = backStackEntry.arguments?.getString("equipmentId")
            LogMaintenanceScreen(navController, equipmentId)
        }

        composable(
            Screen.EditEquipment.route,
            arguments = listOf(navArgument("equipmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val equipmentId = backStackEntry.arguments?.getString("equipmentId")
            EditEquipmentScreen(navController, equipmentId)
        }

        composable(Screen.AddEquipment.route) {
            AddEquipmentScreen(navController)
        }
    }
}