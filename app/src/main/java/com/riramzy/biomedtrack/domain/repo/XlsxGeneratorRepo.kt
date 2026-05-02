package com.riramzy.biomedtrack.domain.repo

import com.riramzy.biomedtrack.domain.model.ReportData
import com.riramzy.biomedtrack.utils.Result
import java.io.OutputStream

interface XlsxGeneratorRepo {
    suspend fun generateExcelReport(
        reportData: ReportData,
        outputStream: OutputStream,
        withLogs: Boolean?
    ): Result<Unit>
}