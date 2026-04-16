package com.riramzy.biomedtrack.utils

import com.riramzy.biomedtrack.domain.model.Department

sealed class Departments(val name: String) {
    data object DialysisUnit : Departments("Dialysis Unit")
    data object BurnUnit : Departments("Burn Unit")
    data object CardiacCareUnit : Departments("Cardiac Care Unit")
    data object CentralSterilizationUnit : Departments("Central Sterilization Unit")
    data object DentalDepartment : Departments("Dental Department")
    data object DentalProstheticsLab : Departments("Dental Prosthetics Lab")
    data object DiabeticFootUnit : Departments("Diabetic Foot Unit")
    data object EchocardiographyUnit : Departments("Echocardiography Unit")
    data object EmergencyRoom : Departments("Emergency Room")
    data object IntensiveCareUnitA : Departments("Intensive Care Unit A")
    data object IntensiveCareUnitB : Departments("Intensive Care Unit B")
    data object Laboratory : Departments("Laboratory")
    data object NeonatalIntensiveCareUnit : Departments("Neonatal Intensive Care Unit")
    data object ObstetricsDepartment : Departments("Obstetrics Department")
    data object OperatingRoom : Departments("Operating Room")
    data object OutpatientClinics : Departments("Outpatient Clinics")
    data object PediatricIntensiveCareUnit : Departments("Pediatric Intensive Care Unit")
    data object PediatricsUnit : Departments("Pediatrics Unit")
    data object PhysiotherapyDepartment : Departments("Physiotherapy Department")
    data object RadiologyDepartment : Departments("Radiology Department")
    data object Reception : Departments("Reception")
    data object SurgeryDepartment : Departments("Surgery Department")

    companion object {
        val all = listOf(
            DialysisUnit.name, BurnUnit.name, CardiacCareUnit.name, CentralSterilizationUnit.name,
            DentalDepartment.name, DentalProstheticsLab.name, DiabeticFootUnit.name,
            EchocardiographyUnit.name, EmergencyRoom.name, IntensiveCareUnitA.name,
            IntensiveCareUnitB.name, Laboratory.name, NeonatalIntensiveCareUnit.name,
            ObstetricsDepartment.name, OperatingRoom.name, OutpatientClinics.name,
            PediatricIntensiveCareUnit.name, PediatricsUnit.name, PhysiotherapyDepartment.name,
            RadiologyDepartment.name, Reception.name, SurgeryDepartment.name
        )
    }
}