package io.github.qobiljon.stressapp.utils

import android.content.Context
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.api.ApiInterface
import io.github.qobiljon.stressapp.core.api.requests.SetFcmTokenRequest
import io.github.qobiljon.stressapp.core.api.requests.SignInRequest
import io.github.qobiljon.stressapp.core.api.requests.SignUpRequest
import io.github.qobiljon.stressapp.core.api.requests.SubmitSelfReportRequest
import io.github.qobiljon.stressapp.core.data.SelfReport
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException

object Api {
    private fun getApiInterface(context: Context): ApiInterface {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(context.getString(R.string.api_base_url)).build().create(ApiInterface::class.java)
    }

    suspend fun signIn(context: Context, email: String, password: String): Boolean {
        return try {
            val result = getApiInterface(context).signIn(SignInRequest(email = email, password = password))
            val resultBody = result.body()
            if (result.errorBody() == null && result.isSuccessful && resultBody != null) {
                Storage.setAuthToken(context, authToken = resultBody.token)
                true
            } else false
        } catch (e: ConnectException) {
            false
        } catch (e: SocketTimeoutException) {
            false
        }
    }

    suspend fun setFcmToken(context: Context, token: String, fcmToken: String): Boolean {
        return try {
            val result = getApiInterface(context).setFcmToken(token = "Token $token", SetFcmTokenRequest(fcm_token = fcmToken))
            result.errorBody() == null && result.isSuccessful
        } catch (e: ConnectException) {
            false
        } catch (e: SocketTimeoutException) {
            false
        }
    }

    suspend fun signUp(context: Context, email: String, fullName: String, gender: String, dateOfBirth: String, fcmToken: String, password: String): Boolean {
        return try {
            val result = getApiInterface(context).signUp(
                SignUpRequest(
                    email = email,
                    full_name = fullName,
                    gender = gender,
                    date_of_birth = dateOfBirth,
                    fcm_token = fcmToken,
                    password = password,
                )
            )
            result.errorBody() == null && result.isSuccessful
        } catch (e: ConnectException) {
            false
        } catch (e: SocketTimeoutException) {
            false
        }
    }

    suspend fun submitSelfReport(context: Context, token: String, selfReport: SelfReport): Boolean {
        return try {
            val result = getApiInterface(context).submitSelfReport(
                token = "Token $token", SubmitSelfReportRequest(
                    timestamp = selfReport.timestamp,
                    pss_control = selfReport.pss_control,
                    pss_confident = selfReport.pss_confident,
                    pss_yourway = selfReport.pss_yourway,
                    pss_difficulties = selfReport.pss_difficulties,
                    stresslvl = selfReport.stresslvl,
                    social_settings = selfReport.social_settings,
                    location = selfReport.location,
                    activity = selfReport.activity,
                )
            )
            result.errorBody() == null && result.isSuccessful
        } catch (e: ConnectException) {
            false
        } catch (e: SocketTimeoutException) {
            false
        }
    }
}