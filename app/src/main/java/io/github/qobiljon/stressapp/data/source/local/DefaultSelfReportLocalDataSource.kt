package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.dao.SelfReportDao
import io.github.qobiljon.stressapp.core.database.data.ActivityRecognition
import io.github.qobiljon.stressapp.core.database.data.SelfReport
import javax.inject.Inject

class DefaultSelfReportLocalDataSource @Inject constructor(
    private val selfReportDao: SelfReportDao
): SelfReportLocalDataSource {

    override fun getSelfReports(): List<SelfReport> {
        return selfReportDao.getAll()
    }

    override fun insertSelfReports(selfReports: List<SelfReport>) {
        return selfReportDao.insertAll(selfReports)
    }

    override fun deleteSelfReport(selfReport: SelfReport) {
        return selfReportDao.delete(selfReport)
    }
}