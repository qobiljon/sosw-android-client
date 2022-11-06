package io.github.qobiljon.stressapp.utils

import android.content.Context
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.api.ApiInterface
import io.github.qobiljon.stressapp.core.api.requests.AuthRequest
import io.github.qobiljon.stressapp.core.api.requests.SubmitSelfReportRequest
import io.github.qobiljon.stressapp.core.data.SelfReport
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    private fun getApiInterface(context: Context): ApiInterface {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(context.getString(R.string.api_base_url)).build().create(ApiInterface::class.java)
    }

    suspend fun authenticate(context: Context, fullName: String, dateOfBirth: String, fcmToken: String): Boolean {
        val result = getApiInterface(context).authenticate(
            AuthRequest(
                full_name = fullName,
                date_of_birth = dateOfBirth,
                fcm_token = fcmToken,
            )
        )
        return result.errorBody() == null
    }

    suspend fun submitEMA(context: Context, fullName: String, dateOfBirth: String, selfReport: SelfReport): Boolean {
        return try {
            val result = getApiInterface(context).submitSelfReport(
                SubmitSelfReportRequest(
                    full_name = fullName,
                    date_of_birth = dateOfBirth,
                    self_report = selfReport,
                )
            )
            result.errorBody() == null && result.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}