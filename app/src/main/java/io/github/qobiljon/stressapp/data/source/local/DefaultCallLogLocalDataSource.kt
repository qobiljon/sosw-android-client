package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.dao.CallLogDao
import io.github.qobiljon.stressapp.core.database.data.CallLog
import javax.inject.Inject

class DefaultCallLogLocalDataSource @Inject constructor(
    private val callLogDao: CallLogDao
) : CallLogLocalDataSource {

    override fun getFilteredCallLogs(submitted: Boolean): List<CallLog> {
        return callLogDao.getFiltered(submitted)
    }

    override fun hasCallLogExist(timestamp: Long): Boolean {
        return callLogDao.exists(timestamp)
    }

    override fun setSubmittedCallLog(submitted: Boolean, timestamp: Long) {
        return callLogDao.setSubmitted(submitted, timestamp)
    }

    override fun insertCallLogs(callLog: List<CallLog>) {
        return callLogDao.insertAll(callLog)
    }
}