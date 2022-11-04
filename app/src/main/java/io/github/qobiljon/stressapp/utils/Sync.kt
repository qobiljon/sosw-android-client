package io.github.qobiljon.stressapp.utils

import java.io.File

object Sync {
    fun submitEMA(q1: Int, q2: Int, q3: Int, q4: Int, q5: Int, q6: String, q7: String, q8: String) {
        for (q in listOf(q1, q2, q3, q4, q5)) if (q < 0 || q > 4) throw IllegalArgumentException("Likert response must be between 0 and 4")
        if (!listOf("social", "asocial").contains(q6)) throw IllegalArgumentException("Social settings must be either \"social\" or \"asocial\"")
        if (!listOf("home", "work", "restaurant", "vehicle", "other").contains(q7)) throw IllegalArgumentException("Location must one of : \"home\", \"work\", \"restaurant\", \"vehicle\", \"other\"")
        if (!listOf("studying_working", "sleeping", "relaxing", "video_watching", "class_meeting", "eating_drinking", "gaming", "conversing", "goingtobed", "calling_texting", "justwokeup", "riding_driving", "other").contains(q8)) throw IllegalArgumentException("Invalid value provided for activity")

        // todo do dis
    }
}