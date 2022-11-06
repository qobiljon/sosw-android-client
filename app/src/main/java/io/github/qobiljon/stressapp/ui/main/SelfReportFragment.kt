package io.github.qobiljon.stressapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.utils.Api
import io.github.qobiljon.stressapp.utils.Storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SelfReportFragment : Fragment(R.layout.fragment_self_report) {
    private lateinit var rgQuestions: List<RadioGroup>
    private lateinit var btnSubmit: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rgQuestions = listOf<RadioGroup>(
            view.findViewById(R.id.rgQuestion1),
            view.findViewById(R.id.rgQuestion2),
            view.findViewById(R.id.rgQuestion3),
            view.findViewById(R.id.rgQuestion4),
            view.findViewById(R.id.rgQuestion5),
            view.findViewById(R.id.rgQuestion6),
            view.findViewById(R.id.rgQuestion7),
            view.findViewById(R.id.rgQuestion8),
        )
        btnSubmit = view.findViewById(R.id.btnSubmit)

        rgQuestions.forEach { rg ->
            rg.setOnCheckedChangeListener { _, i ->
                Log.e("", "${rg.id} - $i")

                var readyToSubmit = true
                for (_rg in rgQuestions) {
                    readyToSubmit = _rg.checkedRadioButtonId != -1
                    if (!readyToSubmit) break
                }
                btnSubmit.isEnabled = readyToSubmit
            }
        }

        btnSubmit.setOnClickListener {
            val q1to5 = mutableListOf(0, 0, 0, 0, 0)
            for (i in 0 until 4) q1to5[i] = when (rgQuestions[i].checkedRadioButtonId) {
                R.id.rbFrequency0 -> 0
                R.id.rbFrequency1 -> 1
                R.id.rbFrequency2 -> 2
                R.id.rbFrequency3 -> 3
                R.id.rbFrequency4 -> 4
                else -> -1
            }
            val socialSettings = when (rgQuestions[5].checkedRadioButtonId) {
                R.id.rbSocial -> "social"
                R.id.rbAsocial -> "asocial"
                else -> ""
            }
            val location = when (rgQuestions[6].checkedRadioButtonId) {
                R.id.rbLocationhome -> "home"
                R.id.rbLocationwork -> "work"
                R.id.rbLocationrestaurant -> "restaurant"
                R.id.rbLocationvehicle -> "vehicle"
                R.id.rbLocationother -> "other"
                else -> ""
            }
            val activity = when (rgQuestions[7].checkedRadioButtonId) {
                R.id.rbActivitystudying -> "studying_working"
                R.id.rbActivitysleeping -> "sleeping"
                R.id.rbActivityrelaxing -> "relaxing"
                R.id.rbActivitywatching -> "video_watching"
                R.id.rbActivityclass -> "class_meeting"
                R.id.rbActivityeating -> "eating_drinking"
                R.id.rbActivitygaming -> "gaming"
                R.id.rbActivityconversing -> "conversing"
                R.id.rbActivitygoingbed -> "goingtobed"
                R.id.rbActivitycalling -> "calling_texting"
                R.id.rbActivityjustwokeup -> "justwokeup"
                R.id.rbActivitydriving -> "riding_driving"
                R.id.rbActivityother -> "other"
                else -> ""
            }

            runBlocking {
                launch {
                    val context = requireActivity().applicationContext
                    Api.submitEMA(
                        context = context,
                        fullName = Storage.getFullName(context),
                        dateOfBirth = Storage.getDateOfBirth(context),
                        pssControl = q1to5[0],
                        pssConfident = q1to5[1],
                        pssYourWay = q1to5[2],
                        pssDifficulties = q1to5[3],
                        stressLvl = q1to5[4],
                        socialSettings = socialSettings,
                        location = location,
                        activity = activity,
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cleanUp()
    }

    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) cleanUp()
    }

    private fun cleanUp() {
        for (rg in rgQuestions) rg.clearCheck()
    }
}
