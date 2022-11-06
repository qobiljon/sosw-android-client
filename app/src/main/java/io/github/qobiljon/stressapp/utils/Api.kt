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

    suspend fun authenticate(context: Context, fullName: String, dateOfBirth: String): Boolean {
        val result = getApiInterface(context).authenticate(
            AuthRequest(full_name = fullName, date_of_birth = dateOfBirth)
        )
        return result.errorBody() != null
    }

    suspend fun submitEMA(
        context: Context,
        fullName: String,
        dateOfBirth: String,
        pssControl: Int,
        pssConfident: Int,
        pssYourWay: Int,
        pssDifficulties: Int,
        stressLvl: Int,
        socialSettings: String,
        location: String,
        activity: String,
    ) {
        for (q in listOf(pssControl, pssConfident, pssYourWay, pssDifficulties, stressLvl)) if (q < 0 || q > 4) throw IllegalArgumentException("Likert response must be between 0 and 4")
        if (!listOf("social", "asocial").contains(socialSettings)) throw IllegalArgumentException("Social settings must be either \"social\" or \"asocial\"")
        if (!listOf("home", "work", "restaurant", "vehicle", "other").contains(location)) throw IllegalArgumentException("Location must one of : \"home\", \"work\", \"restaurant\", \"vehicle\", \"other\"")
        if (!listOf("studying_working", "sleeping", "relaxing", "video_watching", "class_meeting", "eating_drinking", "gaming", "conversing", "goingtobed", "calling_texting", "justwokeup", "riding_driving", "other").contains(activity)) throw IllegalArgumentException("Invalid value provided for activity")

        val timestamp = System.currentTimeMillis()

        val result = getApiInterface(context).submitSelfReport(
            SubmitSelfReportRequest(
                full_name = fullName, date_of_birth = dateOfBirth, self_report = SelfReport(
                    timestamp = System.currentTimeMillis(),
                    pss_control = pssControl,
                    pss_confident = pssConfident,
                    pss_yourway = pssYourWay,
                    pss_difficulties = pssDifficulties,
                    stresslvl = stressLvl,
                    social_settings = socialSettings,
                    location = location,
                    activity = activity,
                )
            )
        )
        if (result.errorBody() == null || !result.isSuccessful) Storage.saveEMA(
            context = context,
            timestamp = timestamp,
            pss_control = pssControl,
            pss_confident = pssConfident,
            pss_yourway = pssYourWay,
            pss_difficulties = pssDifficulties,
            stresslvl = stressLvl,
            social_settings = socialSettings,
            location = location,
            activity = activity,
        )
    }
}