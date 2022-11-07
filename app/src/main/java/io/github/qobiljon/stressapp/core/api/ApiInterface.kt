package io.github.qobiljon.stressapp.core.api

import io.github.qobiljon.stressapp.core.api.requests.*
import io.github.qobiljon.stressapp.core.api.responses.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("auth")
    suspend fun authenticate(@Body request: AuthRequest): Response<EmptyResponse>

    @POST("submit_ema")
    suspend fun submitSelfReport(@Body submitSelfReportRequest: SubmitSelfReportRequest): Response<BasicResponse>
}