package com.riramzy.biomedtrack.utils

import java.util.UUID

data class DataPreviewRow(
    val id: String = UUID.randomUUID().toString(),
    val isSelected: Boolean = false,
    val name: String,
    val model: String,
    val serialNumber: String,
    val department: String,
    val category: String,
    val status: String,
    val logs: String,
    val validationStatus: ValidationStatus = ValidationStatus.VALID,
    val message: String? = null
)