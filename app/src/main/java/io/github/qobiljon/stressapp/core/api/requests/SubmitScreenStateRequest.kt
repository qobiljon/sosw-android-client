package io.github.qobiljon.stressapp.core.api.requests

data class SubmitScreenStateRequest(
    val timestamp: Long,
    val screen_state: String,
    val keyguard_restricted_input_mode: Boolean,
)
