package io.github.qobiljon.stressapp.core.api

import io.github.qobiljon.stressapp.core.api.requests.*
import io.github.qobiljon.stressapp.core.api.responses.*
import io.github.qobiljon.stressapp.core.data.SelfReport
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiInterface {
    @POST("sign_in")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST("sign_up")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @PUT("set_fcm_token")
    suspend fun setFcmToken(@Header("Authorization") token: String, @Body fcmTokenRequest: SetFcmTokenRequest): Response<Void>

    @POST("submit_self_report")
    suspend fun submitSelfReport(@Header("Authorization") token: String, @Body submitSelfReportRequest: SubmitSelfReportRequest): Response<Void>
}