package com.riramzy.biomedtrack.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.riramzy.biomedtrack.R

@Composable
fun getLocalizedDepartmentName(name: String): String {
    val resourceId = when (name) {
        "Dialysis Unit" -> R.string.dept_dialysis_unit
        "Burn Unit" -> R.string.dept_burn_unit
        "Cardiac Care Unit" -> R.string.dept_cardiac_care_unit
        "Central Sterilization Unit" -> R.string.dept_central_sterilization_unit
        "Dental Department" -> R.string.dept_dental_department
        "Dental Prosthetics Lab" -> R.string.dept_dental_prosthetics_lab
        "Diabetic Foot Unit" -> R.string.dept_diabetic_foot_unit
        "Echocardiography Unit" -> R.string.dept_echocardiography_unit
        "Emergency Room" -> R.string.dept_emergency_room
        "Intensive Care Unit A" -> R.string.dept_intensive_care_unit_a
        "Intensive Care Unit B" -> R.string.dept_intensive_care_unit_b
        "Laboratory" -> R.string.dept_laboratory
        "Neonatal Intensive Care Unit" -> R.string.dept_neonatal_intensive_care_unit
        "Obstetrics Department" -> R.string.dept_obstetrics_department
        "Operating Room" -> R.string.dept_operating_room
        "Outpatient Clinics" -> R.string.dept_outpatient_clinics
        "Pediatric Intensive Care Unit" -> R.string.dept_pediatric_intensive_care_unit
        "Pediatrics Unit" -> R.string.dept_pediatrics_unit
        "Physiotherapy Department" -> R.string.dept_physiotherapy_department
        "Radiology Department", "Radiology" -> R.string.dept_radiology_department
        "Reception" -> R.string.dept_reception
        "Surgery Department" -> R.string.dept_surgery_department
        else -> null
    }

    return if (resourceId != null) {
        stringResource(resourceId)
    } else {
        if (name.contains("Department", ignoreCase = true) ||
            name.contains("Unit", ignoreCase = true) ||
            name.contains("Room", ignoreCase = true) ||
            name.contains("Lab", ignoreCase = true) ||
            name.contains("Clinics", ignoreCase = true) ||
            name.contains("Reception", ignoreCase = true)
        ) {
            name
        } else {
            "$name Department"
        }
    }
}

fun EquipmentStatus.getStringResId(): Int {
    return when (this) {
        EquipmentStatus.ONLINE -> R.string.status_online
        EquipmentStatus.DOWN -> R.string.status_down
        EquipmentStatus.SERVICE -> R.string.status_service
    }
}