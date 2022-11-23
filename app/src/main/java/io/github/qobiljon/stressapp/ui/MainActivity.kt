package io.github.qobiljon.stressapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.services.DataSubmissionService
import io.github.qobiljon.stressapp.ui.main.SectionsPagerAdapter
import io.github.qobiljon.stressapp.utils.Api
import io.github.qobiljon.stressapp.utils.Storage
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_AUTH = 102
        const val TAG = "StressApp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        viewPager.adapter = sectionsPagerAdapter

        @Suppress("DEPRECATION") if (!Storage.isAuthenticated(applicationContext)) startActivityForResult(Intent(applicationContext, AuthActivity::class.java), REQUEST_CODE_AUTH)
        else runServices()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION") super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_AUTH) {
            if (resultCode == 0) runServices()
            else finish()
        }
    }

    override fun onResume() {
        super.onResume()

//        val llDashboard = findViewById<LinearLayout>(R.id.llDashboard)
//        llDashboard.removeAllViews()

//        val selfReportDao = Storage.db.selfReportDao()
//        runBlocking {
//            val tmp = selfReportDao.getK(k = 1)
//            if (tmp.isEmpty()) return@runBlocking
//
//            val then = Calendar.getInstance()
//            val now = Calendar.getInstance()
//            then.timeInMillis = tmp[0].timestamp
//            listOf(then, now).forEach {
//                it.set(Calendar.MINUTE, 0)
//                it.set(Calendar.SECOND, 0)
//                it.set(Calendar.MILLISECOND, 0)
//            }
//
//            var prevDate = ""
//            val hours = listOf("0am", "1am", "2am", "3am", "4am", "5am", "6am", "7am", "8am", "9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm")
//            var amounts = (0 until 24).map { 0 }
//            while (then < now) {
//                val date = "${then.get(Calendar.MONTH)}/${then.get(Calendar.DAY_OF_MONTH)}"
//                val hour = "${then.get(Calendar.HOUR)}/${if (then.get(Calendar.AM_PM) == Calendar.AM) "am" else "pm"}"
//
//                if (prevDate != date) {
//                    if (prevDate.isNotEmpty()) {
//                        // val view =
//                        // llDashboard.addView()
//                    }
//
//                    prevDate = date
//                    amounts = (0 until 24).map { 0 }
//                }
//
//                // if (date in amountPerHour) {
//                //    if (hour in amountPerHour[date].contains())
//                //        amountPerHour[date][hour] += 1
//                //}
//                then.add(Calendar.HOUR, 1)
//            }
//
//            for (selfReport in selfReportDao.getAll()) {
//                val success = Api.submitSelfReport(applicationContext, token = Storage.getAuthToken(applicationContext), selfReport = selfReport)
//                if (success) selfReportDao.setIsSubmitted(timestamp = selfReport.timestamp, isSubmitted = true)
//            }
//        }
    }

    private fun runServices() {
        startForegroundService(Intent(applicationContext, DataSubmissionService::class.java))
    }
}