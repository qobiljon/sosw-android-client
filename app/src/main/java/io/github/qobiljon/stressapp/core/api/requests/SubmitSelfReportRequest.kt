package io.github.qobiljon.stressapp.core.api.requests

data class SubmitSelfReportRequest(
    val timestamp: Long,
    val pss_control: Int,
    val pss_confident: Int,
    val pss_yourway: Int,
    val pss_difficulties: Int,
    val stresslvl: Int,
    val social_settings: String,
    val location: String,
    val activity: String,
)
