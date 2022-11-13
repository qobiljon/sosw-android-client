package io.github.qobiljon.stressapp.core.api.responses

data class SignUpResponse(
    val email: String,
    val full_name: String,
    val gender: String,
    val date_of_birth: String,
    val fcm_token: String,
)