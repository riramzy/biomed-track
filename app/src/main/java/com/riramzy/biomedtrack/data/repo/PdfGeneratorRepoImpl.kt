package com.riramzy.biomedtrack.data.repo

import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.riramzy.biomedtrack.domain.model.ReportData
import com.riramzy.biomedtrack.domain.repo.PdfGeneratorRepo
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.Timestamps.toDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import javax.inject.Inject

class PdfGeneratorRepoImpl @Inject constructor(): PdfGeneratorRepo {
    override suspend fun generatePdfReport(
        reportData: ReportData,
        outputStream: OutputStream,
        withLogs: Boolean?
    ): Result<Unit> = withContext(Dispatchers.IO) {
        outputStream.use {
            try {
                val pdfWriter = PdfWriter(outputStream)
                val pdfDocument = PdfDocument(pdfWriter)
                val document = Document(pdfDocument)

                val primaryColor = DeviceRgb(0, 102, 204)

                document.add(
                    Paragraph("BioMed Track - Equipment Report")
                        .setFontSize(24f)
                        .setBold()
                        .setFontColor(primaryColor)
                        .setTextAlignment(TextAlignment.CENTER)
                )

                document.add(
                    Paragraph("Generated on: ${reportData.generatedAt} by: ${reportData.generatedBy}")
                        .setFontSize(12f)
                        .setItalic()
                        .setTextAlignment(TextAlignment.CENTER)
                )

                document.add(Paragraph("\n"))

                val departmentName = reportData.department?.name ?: "All Departments"
                val dateRange = "${reportData.startDate.toDateString()}:${reportData.endDate.toDateString()}"

                document.add(Paragraph("Department: $departmentName").setBold())
                document.add((Paragraph("Period: $dateRange")))
                document.add(Paragraph("\n"))

                val summaryTable = Table(4).useAllAvailableWidth()

                summaryTable.addHeaderCell("Total Equipment")
                summaryTable.addHeaderCell("Healthy")
                summaryTable.addHeaderCell("Due Service")
                summaryTable.addHeaderCell("Down")

                val total = reportData.equipmentList.size
                val healthy = reportData.equipmentList.count { it.status == EquipmentStatus.ONLINE }
                val dueService = reportData.equipmentList.count { it.status == EquipmentStatus.SERVICE }
                val down = reportData.equipmentList.count { it.status == EquipmentStatus.DOWN }

                summaryTable.addCell(
                    Cell().add(Paragraph(total.toString()).setTextAlignment(TextAlignment.CENTER))
                )
                summaryTable.addCell(
                    Cell().add(Paragraph(healthy.toString()).setTextAlignment(TextAlignment.CENTER))
                )
                summaryTable.addCell(
                    Cell().add(Paragraph(dueService.toString()).setTextAlignment(TextAlignment.CENTER))
                )
                summaryTable.addCell(
                    Cell().add(Paragraph(down.toString()).setTextAlignment(TextAlignment.CENTER))
                )

                document.add(summaryTable)
                document.add(Paragraph("\n"))

                document.add(Paragraph("Equipment Inventory")
                    .setFontSize(16f)
                    .setBold()
                    .setFontColor(primaryColor)
                )

                val equipmentTable = Table(floatArrayOf(3f, 2f, 2f, 2f, 1f)).useAllAvailableWidth()

                equipmentTable.addHeaderCell("Name")
                equipmentTable.addHeaderCell("Model")
                equipmentTable.addHeaderCell("Serial")
                equipmentTable.addHeaderCell("Department")
                equipmentTable.addHeaderCell("Status")

                reportData.equipmentList.forEach { equipment ->
                    equipmentTable.addCell(Cell().add(Paragraph(equipment.name).setFontSize(9f)))
                    equipmentTable.addCell(Cell().add(Paragraph(equipment.model).setFontSize(9f)))
                    equipmentTable.addCell(Cell().add(Paragraph(equipment.serialNumber).setFontSize(9f)))
                    equipmentTable.addCell(Cell().add(Paragraph(equipment.department.name).setFontSize(9f)))
                    equipmentTable.addCell(Cell().add(Paragraph(equipment.status.name).setFontSize(9f)))
                }

                document.add(equipmentTable)

                if (withLogs == true && reportData.maintenanceLogs.isNotEmpty()) {
                    document.add(Paragraph("\n"))
                    document.add(Paragraph("Maintenance History").setFontSize(16f).setBold().setFontColor(primaryColor))

                    val logsTable = Table(floatArrayOf(2f, 2f, 2f, 2f, 3f)).useAllAvailableWidth()

                    logsTable.addHeaderCell("Date")
                    logsTable.addHeaderCell("Equipment")
                    logsTable.addHeaderCell("Type")
                    logsTable.addHeaderCell("By")
                    logsTable.addHeaderCell("Work Done")

                    reportData.maintenanceLogs.forEach { log ->
                        logsTable.addCell(Cell().add(Paragraph(log.date.toDateString()).setFontSize(9f)))
                        logsTable.addCell(Cell().add(Paragraph(log.equipmentName).setFontSize(9f)))
                        logsTable.addCell(Cell().add(Paragraph(log.type.name).setFontSize(9f)))
                        logsTable.addCell(Cell().add(Paragraph(log.technicianName).setFontSize(9f)))
                        logsTable.addCell(Cell().add(Paragraph(log.workDone).setFontSize(9f)))
                    }

                    document.add(logsTable)
                }

                document.close()

                return@withContext Result.Success(Unit)
            } catch (e: Exception) {
                return@withContext Result.Error(message = e.message ?: "Failed to generate PDF report")
            }
        }
    }
}