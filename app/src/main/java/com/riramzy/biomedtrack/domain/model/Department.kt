package com.riramzy.biomedtrack.domain.model

data class Department(
    val id: String,
    val name: String,
    val totalEquipment: Int,
    val dueServiceEquipment: Int? = 0,
    val downEquipment: Int? = 0
)