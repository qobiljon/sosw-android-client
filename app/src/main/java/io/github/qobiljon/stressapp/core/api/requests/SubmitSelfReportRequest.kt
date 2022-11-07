package io.github.qobiljon.stressapp.core.api.requests

import io.github.qobiljon.stressapp.core.data.SelfReport

data class SubmitSelfReportRequest(
    val full_name: String,
    val date_of_birth: String,
    val self_reports: List<SelfReport>,
)