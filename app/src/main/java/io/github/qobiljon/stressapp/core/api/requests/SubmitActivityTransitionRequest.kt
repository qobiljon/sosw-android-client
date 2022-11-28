package io.github.qobiljon.stressapp.core.api.requests

data class SubmitActivityTransitionRequest(
    val timestamp: Long,
    val activity_type: String,
    val transition_type: String,
)

