package io.github.qobiljon.stressapp.core.api.requests

data class SubmitCalendarEventRequest(
    val event_id: String,
    val title: String,
    val start_ts: Long,
    val end_ts: Long,
    val event_location: String,
)
