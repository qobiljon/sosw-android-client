package io.github.qobiljon.stressapp.core.api

import io.github.qobiljon.stressapp.core.api.requests.*
import io.github.qobiljon.stressapp.core.api.responses.*
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

    @POST("submit_location")
    suspend fun submitLocation(@Header("Authorization") token: String, @Body submitLocationRequest: SubmitLocationRequest): Response<Void>

    @POST("submit_call_log")
    suspend fun submitCallLog(@Header("Authorization") token: String, @Body submitCallLogRequest: SubmitCallLogRequest): Response<Void>

    @POST("submit_calendar_event")
    suspend fun submitCalendarEvent(@Header("Authorization") token: String, @Body submitCalendarEventRequest: SubmitCalendarEventRequest): Response<Void>

    @POST("submit_activity_transition")
    suspend fun submitActivityTransition(@Header("Authorization") token: String, @Body submitActivityTransitionRequest: SubmitActivityTransitionRequest): Response<Void>

    @POST("submit_screen_state")
    suspend fun submitScreenState(@Header("Authorization") token: String, @Body submitScreenStateRequest: SubmitScreenStateRequest): Response<Void>
}
