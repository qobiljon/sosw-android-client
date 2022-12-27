package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.data.CallLog

interface CallLogLocalDataSource {
    fun getFilteredCallLogs(submitted: Boolean): List<CallLog>
    fun hasCallLogExist(timestamp: Long): Boolean
    fun setSubmittedCallLog(submitted: Boolean, timestamp: Long)
    fun insertCallLogs(callLog: List<CallLog>)
}