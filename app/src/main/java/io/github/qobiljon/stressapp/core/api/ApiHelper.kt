package io.github.qobiljon.stressapp.core.api

import android.content.Context
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.api.requests.*
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.core.database.data.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException

object ApiHelper {
    private fun getApiInterface(context: Context): ApiInterface {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(context.getString(R.string.api_base_url)).build().create(ApiInterface::class.java)
    }

    suspend fun signIn(context: Context, email: String, password: String): Boolean {
        return try {
            val result = getApiInterface(context).signIn(SignInRequest(email = email, password = password))
            val resultBody = result.body()
            if (result.errorBody() == null && result.isSuccessful && resultBody != null) {
                DatabaseHelper.setAuthToken(context, authToken = resultBody.token)
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

    suspend fun submitLocation(context: Context, token: String, location: Location): Boolean {
        return try {
            val result = getApiInterface(context).submitLocation(
                token = "Token $token", SubmitLocationRequest(
                    timestamp = location.timestamp,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    accuracy = location.accuracy,
                )
            )
            result.errorBody() == null && result.isSuccessful
        } catch (e: ConnectException) {
            false
        } catch (e: SocketTimeoutException) {
            false
        }
    }

    suspend fun submitScreenState(context: Context, token: String, screenState: ScreenState): Boolean {
        return try {
            val result = getApiInterface(context).submitScreenState(
                token = "Token $token", SubmitScreenStateRequest(
                    timestamp = screenState.timestamp,
                    screen_state = screenState.screen_state,
                    keyguard_restricted_input_mode = screenState.keyguard_restricted_input_mode,
                )
            )
            result.errorBody() == null && result.isSuccessful
        } catch (e: ConnectException) {
            false
        } catch (e: SocketTimeoutException) {
            false
        }
    }

    suspend fun submitCallLog(context: Context, token: String, callLog: CallLog): Boolean {
        return try {
            val result = getApiInterface(context).submitCallLog(
                token = "Token $token", SubmitCallLogRequest(
                    timestamp = callLog.timestamp,
                    number = callLog.number,
                    duration = callLog.duration,
                    call_type = callLog.call_type,
                )
            )
            result.errorBody() == null && result.isSuccessful
        } catch (e: ConnectException) {
            false
        } catch (e: SocketTimeoutException) {
            false
        }
    }

    suspend fun submitCalendarEvent(context: Context, token: String, calendarEvent: CalendarEvent): Boolean {
        return try {
            val result = getApiInterface(context).submitCalendarEvent(
                token = "Token $token", SubmitCalendarEventRequest(
                    event_id = calendarEvent.event_id,
                    title = calendarEvent.title,
                    start_ts = calendarEvent.start_ts,
                    end_ts = calendarEvent.end_ts,
                    event_location = calendarEvent.event_location ?: "",
                )
            )
            result.errorBody() == null && result.isSuccessful
        } catch (e: ConnectException) {
            false
        } catch (e: SocketTimeoutException) {
            false
        }
    }

    suspend fun submitActivityTransition(context: Context, token: String, activityTransition: ActivityTransition): Boolean {
        return try {
            val result = getApiInterface(context).submitActivityTransition(
                token = "Token $token", SubmitActivityTransitionRequest(
                    timestamp = activityTransition.timestamp,
                    activity_type = activityTransition.activity_type,
                    transition_type = activityTransition.transition_type,
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
