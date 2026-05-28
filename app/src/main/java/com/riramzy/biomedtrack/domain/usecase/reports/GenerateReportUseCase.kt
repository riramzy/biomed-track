package com.riramzy.biomedtrack.domain.usecase.reports

import com.riramzy.biomedtrack.di.SessionManager
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.domain.model.ReportData
import com.riramzy.biomedtrack.domain.permission.Permission
import com.riramzy.biomedtrack.domain.repo.EquipmentRepo
import com.riramzy.biomedtrack.domain.repo.MaintenanceRepo
import com.riramzy.biomedtrack.utils.UserRole
import jakarta.inject.Inject
import java.time.LocalDate

class GenerateReportUseCase @Inject constructor(
    private val equipmentRepository: EquipmentRepo,
    private val maintenanceRepo: MaintenanceRepo,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(
        startDate: Long,
        endDate: Long,
        department: Department?,
        includeHealthy: Boolean,
        includeDown: Boolean,
        includeServiceDue: Boolean
    ): Result<ReportData> {
        val currentUser = sessionManager.currentUser.value ?: return Result.Error("User not logged in")

        if (!sessionManager.hasPermission(Permission.GENERATE_REPORTS)) {
            return Result.Error("User doesn't have permission to generate reports")
        }

        val allEquipment = equipmentRepository.getAllEquipmentOnce()

        val isTechnician = currentUser.role == UserRole.TECHNICIAN

        val assignedDepartmentsNames = currentUser.assignedDepartments.map { it.name }.toSet()

        if (isTechnician && department != null && department.name !in assignedDepartmentsNames) {
            return Result.Error("Access Denied: You do not have permission to generate reports for this department")
        }

        val baseEquipment = if (isTechnician) {
            allEquipment.filter { it.department.name in assignedDepartmentsNames }
        } else {
            allEquipment
        }

        val equipmentFilteredByDepartment = if (department == null) {
            baseEquipment
        } else {
            baseEquipment.filter { it.department == department }
        }

        val filteredEquipment = equipmentFilteredByDepartment.filter { equipment ->
            (includeHealthy && equipment.status == EquipmentStatus.ONLINE) ||
                    (includeDown && equipment.status == EquipmentStatus.DOWN) ||
                    (includeServiceDue && equipment.status == EquipmentStatus.SERVICE)
        }

        val allLogs = maintenanceRepo.getLogsByDateRange(startDate, endDate, department)

        val logs = if (isTechnician) {
            allLogs.filter { it.department.name in assignedDepartmentsNames }
        } else {
            allLogs
        }

        return Result.Success(ReportData(
            equipmentList = filteredEquipment,
            maintenanceLogs = logs,
            department = department,
            startDate = startDate,
            endDate = endDate,
            generatedAt = LocalDate.now().toString(),
            generatedBy = sessionManager.currentUser.value?.name ?: ""
        ))
    }
}