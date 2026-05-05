package com.riramzy.biomedtrack.domain.usecase.equipment

import com.riramzy.biomedtrack.ui.components.importing.DataPreviewRow
import com.riramzy.biomedtrack.ui.components.importing.ValidationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream
import javax.inject.Inject

data class RawEquipment(
    val name: String,
    val model: String,
    val serialNumber: String,
    val manufacturer: String,
    val agent: String,
    val category: String,
    val departmentName: String,
    val location: String,
    val status: String,
    val installDate: String,
    val contractBy: String,
    val contractEndDate: String,
    val warrantyEndDate: String
)

class ExcelParserUseCase @Inject constructor() {
    private val formatter = DataFormatter()

    suspend operator fun invoke(
        inputStream: InputStream,
        existingEquipmentIds: Set<String>
    ): Triple<List<DataPreviewRow>, List<RawEquipment>, Set<String>> = withContext(Dispatchers.IO) {
        val previewRows = mutableListOf<DataPreviewRow>()
        val rawEquipmentList = mutableListOf<RawEquipment>()
        val uniqueDepartments = mutableSetOf<String>()

        WorkbookFactory.create(inputStream).use { workbook ->
            val sheet = workbook.getSheetAt(0)

            for (row in 2..sheet.lastRowNum) {
                val rowData = sheet.getRow(row) ?: continue

                val name = formatter.formatCellValue(rowData.getCell(0))
                val serialNumber = formatter.formatCellValue(rowData.getCell(2))
                val department = formatter.formatCellValue(rowData.getCell(6))

                if (serialNumber.isBlank() && name.isBlank()) continue
                if (department.isNotBlank()) uniqueDepartments.add(department)

                val isDuplicate = existingEquipmentIds.contains(serialNumber)
                val status = if (serialNumber.isBlank() || isDuplicate) ValidationStatus.ERROR else ValidationStatus.VALID
                val message = if (isDuplicate) "Serial Number already exists" else null

                previewRows.add(
                    DataPreviewRow(
                        name = name,
                        model = formatter.formatCellValue(rowData.getCell(1)),
                        serialNumber = serialNumber,
                        department = department,
                        category = formatter.formatCellValue(rowData.getCell(5)),
                        status = formatter.formatCellValue(rowData.getCell(8)),
                        "",
                        validationStatus = status,
                        message = message
                    )
                )

                if (status == ValidationStatus.VALID) {
                    rawEquipmentList.add(
                        RawEquipment(
                            name = name,
                            model = formatter.formatCellValue(rowData.getCell(1)),
                            serialNumber = serialNumber,
                            manufacturer = formatter.formatCellValue(rowData.getCell(3)),
                            agent = formatter.formatCellValue(rowData.getCell(4)),
                            category = formatter.formatCellValue(rowData.getCell(5)),
                            departmentName = department,
                            location = formatter.formatCellValue(rowData.getCell(7)),
                            status = formatter.formatCellValue(rowData.getCell(8)),
                            installDate = formatter.formatCellValue(rowData.getCell(9)),
                            contractBy = formatter.formatCellValue(rowData.getCell(10)),
                            contractEndDate = formatter.formatCellValue(rowData.getCell(11)),
                            warrantyEndDate = formatter.formatCellValue(rowData.getCell(12))
                        )
                    )
                }
            }
        }
        Triple(previewRows, rawEquipmentList, uniqueDepartments)
    }
}