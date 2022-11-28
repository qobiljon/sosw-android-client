package io.github.qobiljon.stressapp.core.api.requests

data class SubmitCallLogRequest(
    val timestamp: Long,
    val number: String,
    val duration: String,
    val call_type: String,
)
