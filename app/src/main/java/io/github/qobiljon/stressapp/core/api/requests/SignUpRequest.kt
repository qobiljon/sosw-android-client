package io.github.qobiljon.stressapp.core.api.requests

data class SignUpRequest(
    val email: String,
    val full_name: String,
    val gender: String,
    val date_of_birth: String,
    val fcm_token: String,
    val password: String,
)