package io.github.qobiljon.stressapp.core.api.requests

data class SubmitActivityRecognitionRequest(
    val timestamp: Long,
    val activity: String,
    val confidence: Int,
)