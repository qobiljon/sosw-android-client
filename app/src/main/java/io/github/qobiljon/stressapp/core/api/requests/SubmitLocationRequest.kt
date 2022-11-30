package io.github.qobiljon.stressapp.core.api.requests

data class SubmitLocationRequest(
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
)
