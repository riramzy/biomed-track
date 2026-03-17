package com.riramzy.biomedtrack.domain.model

// Represent a single task in a maintenance checklist like "Cleaned filters" or "Calibrated pressure"
data class ChecklistItem(
    val id: String,
    val label: String,
    val isChecked: Boolean
)
