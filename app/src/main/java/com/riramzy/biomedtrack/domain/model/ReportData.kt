package com.riramzy.biomedtrack.domain.model

import java.time.LocalDate

data class ReportData (
    val equipmentList: List<Equipment>,
    val maintenanceLogs: List<MaintenanceLog>,
    val department: Department?,
    val startDate: String,
    val endDate: String,
    val generatedAt: String = LocalDate.now().toString(),
    val generatedBy: String
)