package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.data.ActivityRecognition
import io.github.qobiljon.stressapp.core.database.data.SelfReport

interface SelfReportLocalDataSource {
    fun getSelfReports(): List<SelfReport>
    fun insertSelfReports(selfReports: List<SelfReport>)
    fun deleteSelfReport(selfReport: SelfReport)
}