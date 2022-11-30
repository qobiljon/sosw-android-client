package io.github.qobiljon.stressapp.core.api.requests

data class SubmitActivityTransitionRequest(
    val timestamp: Long,
    val activity: String,
    val transition: String,
)