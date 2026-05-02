package com.riramzy.biomedtrack.data.repo

import com.riramzy.biomedtrack.domain.model.ReportData
import com.riramzy.biomedtrack.domain.repo.XlsxGeneratorRepo
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.Timestamps.toDateString
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

class XlsxGeneratorRepoImpl @Inject constructor(): XlsxGeneratorRepo {
    override suspend fun generateExcelReport(
        reportData: ReportData,
        outputStream: OutputStream,
        withLogs: Boolean?
    ): Result<Unit> = withContext(Dispatchers.IO) {
        outputStream.use { stream ->
            try {
                val workbook = XSSFWorkbook()
                val headerFont = workbook.createFont().apply { bold = true }
                val headerStyle = workbook.createCellStyle().apply { setFont(headerFont) }

                val sheet1 = workbook.createSheet("Equipment Inventory")
                val headers1 = listOf("Name", "Model", "Serial", "Department", "Status", "Last Maintenance")
                val headerRow1 = sheet1.createRow(0)

                headers1.forEachIndexed { index, title ->
                    headerRow1.createCell(index).apply {
                        setCellValue(title)
                        cellStyle = headerStyle
                    }
                }

                reportData.equipmentList.forEach {
                    val row = sheet1.createRow(sheet1.lastRowNum + 1)
                    row.createCell(0).setCellValue(it.name)
                    row.createCell(1).setCellValue(it.model)
                    row.createCell(2).setCellValue(it.serialNumber)
                    row.createCell(3).setCellValue(it.department.name)
                    row.createCell(4).setCellValue(it.status.name)
                    row.createCell(5).setCellValue(it.lastMaintenanceDate?.toDateString() ?: "N/A")
                }

                headers1.indices.forEach {
                    sheet1.autoSizeColumn(it)
                }

                if (withLogs == true && reportData.maintenanceLogs.isNotEmpty()) {
                    val sheet2 = workbook.createSheet("Maintenance History")
                    val headers2 = listOf("Date", "Equipment", "Type", "Technician", "Status After", "Work Done")
                    val headerRow2 = sheet2.createRow(0)

                    headers2.forEachIndexed { index, title ->
                        headerRow2.createCell(index).apply {
                            setCellValue(title)
                            cellStyle = headerStyle
                        }
                    }

                    reportData.maintenanceLogs.forEach {
                        val row = sheet2.createRow(sheet2.lastRowNum + 1)
                        row.createCell(0).setCellValue(it.date.toDateString())
                        row.createCell(1).setCellValue(it.equipmentName)
                        row.createCell(2).setCellValue(it.type.name)
                        row.createCell(3).setCellValue(it.technicianName)
                        row.createCell(4).setCellValue(it.currentStatus.name)
                        row.createCell(5).setCellValue(it.workDone)
                    }

                    headers2.indices.forEach {
                        sheet2.autoSizeColumn(it)
                    }
                }

                workbook.write(stream)
                workbook.close()

                return@withContext Result.Success(Unit)
            } catch (e: Exception) {
                return@withContext Result.Error(message = e.message ?: "Failed to generate Excel report")
            }
        }
    }
}