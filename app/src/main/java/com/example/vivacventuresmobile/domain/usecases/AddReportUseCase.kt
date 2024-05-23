package com.example.vivacventuresmobile.domain.usecases

import com.example.vivacventuresmobile.data.repositories.ReportsRepository
import com.example.vivacventuresmobile.domain.modelo.Report
import javax.inject.Inject

class AddReportUseCase @Inject constructor(private var repository: ReportsRepository) {
    operator fun invoke(report: Report) = repository.saveReport(report)
}