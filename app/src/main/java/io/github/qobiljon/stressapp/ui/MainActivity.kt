package io.github.qobiljon.stressapp.ui

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.api.ApiHelper
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.core.database.data.SelfReport
import io.github.qobiljon.stressapp.services.DataCollectionService
import io.github.qobiljon.stressapp.services.DataSubmissionService
import io.github.qobiljon.stressapp.utils.Utils
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
        private const val REQUEST_CODE_AUTH = 102
        private val permissions = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.RECORD_AUDIO,
        )
        const val TAG = "StressApp"
    }

    private lateinit var rgQuestions: List<RadioGroup>
    private lateinit var btnSubmit: Button
    private var collectSvc: DataCollectionService? = null
    private var collectSvcBound: Boolean = false
    private val collectSvcCon = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as DataCollectionService.LocalBinder
            collectSvc = binder.getService

            if (!binder.getService.isRunning) {
                val intent = Intent(applicationContext, DataCollectionService::class.java)
                startForegroundService(intent)
            }

            collectSvcBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            collectSvcBound = false
        }
    }
    private var submitSvc: DataSubmissionService? = null
    private var submitSvcBound: Boolean = false
    private val submitSvcCon = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as DataSubmissionService.LocalBinder
            submitSvc = binder.getService

            if (!binder.getService.isRunning) {
                val intent = Intent(applicationContext, DataSubmissionService::class.java)
                startForegroundService(intent)
            }

            submitSvcBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            submitSvcBound = false
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            // TODO: Inform user that that your app will not show notifications.
            Utils.toast(applicationContext, "Problem with PUSH notifications, please report this case to researchers!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rgQuestions = listOf<RadioGroup>(
            findViewById(R.id.rgQuestion1),
            findViewById(R.id.rgQuestion2),
            findViewById(R.id.rgQuestion3),
            findViewById(R.id.rgQuestion4),
            findViewById(R.id.rgQuestion5),
            findViewById(R.id.rgQuestion6),
            findViewById(R.id.rgQuestion7),
            findViewById(R.id.rgQuestion8),
        )
        btnSubmit = findViewById(R.id.btnSubmit)

        rgQuestions.forEach { rg ->
            rg.setOnCheckedChangeListener { _, _ ->
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
                    val context = applicationContext
                    val selfReport = SelfReport(
                        timestamp = System.currentTimeMillis(),
                        pss_control = q1to5[0],
                        pss_confident = q1to5[1],
                        pss_yourway = q1to5[2],
                        pss_difficulties = q1to5[3],
                        stresslvl = q1to5[4],
                        social_settings = socialSettings,
                        location = location,
                        activity = activity,
                    )
                    val success = ApiHelper.submitSelfReport(
                        context = context,
                        token = DatabaseHelper.getAuthToken(context),
                        selfReport = selfReport,
                    )
                    if (!success) DatabaseHelper.saveSelfReport(selfReport)
                    cleanUp()
                }
            }
        }

        @Suppress("DEPRECATION") if (!DatabaseHelper.isAuthenticated(applicationContext)) startActivityForResult(Intent(applicationContext, AuthActivity::class.java), REQUEST_CODE_AUTH)
        else runServices()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        run {
            val dao = DatabaseHelper.db.locationDao()
            dao.getAll().forEach { l ->
                Log.e(TAG, "old loc: ${l.timestamp}, ${l.latitude}, ${l.longitude}, ${l.accuracy}")
            }
        }
        run {
            val dao = DatabaseHelper.db.activityTransitionDao()
            dao.getAll().forEach { a ->
                Log.e(TAG, "old activity: ${a.timestamp}, ${a.activity_type}, ${a.transition_type}")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION") super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_AUTH) {
            if (resultCode == 12) runServices()
            else finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        cleanUp()

        val missingPermissions = permissions.filter { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
        if (missingPermissions.isEmpty()) {
            if (DatabaseHelper.isAuthenticated(applicationContext)) {
                val collectionIntent = Intent(applicationContext, DataCollectionService::class.java)
                bindService(collectionIntent, collectSvcCon, BIND_AUTO_CREATE)
                val submissionIntent = Intent(applicationContext, DataSubmissionService::class.java)
                bindService(submissionIntent, submitSvcCon, BIND_AUTO_CREATE)
            }
        } else requestPermissions(missingPermissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isEmpty()) throw java.lang.RuntimeException("Empty permission results")
        if (requestCode == PERMISSION_REQUEST_CODE) {
            Log.e("DATA", "GRANTED ${grantResults[0] == PackageManager.PERMISSION_GRANTED}")
            val missingPermissions = Companion.permissions.filter { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Utils.toast(applicationContext, getString(R.string.permissions_toast))
                finishAffinity()
            } else if (missingPermissions.isEmpty()) {
                val collectionIntent = Intent(applicationContext, DataCollectionService::class.java)
                bindService(collectionIntent, collectSvcCon, BIND_AUTO_CREATE)
                val submissionIntent = Intent(applicationContext, DataSubmissionService::class.java)
                bindService(submissionIntent, submitSvcCon, BIND_AUTO_CREATE)
            }
        }
    }

    private fun runServices() {
        startForegroundService(Intent(applicationContext, DataSubmissionService::class.java))
    }

    private fun cleanUp() {
        for (rg in rgQuestions) rg.clearCheck()
    }
}
