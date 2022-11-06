package io.github.qobiljon.stressapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.utils.Api
import io.github.qobiljon.stressapp.utils.Storage
import io.github.qobiljon.stressapp.utils.Utils
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etDateOfBirth = findViewById<EditText>(R.id.etDateOfBirth)
        val btnAuthenticate = findViewById<Button>(R.id.btnAuthenticate)
        btnAuthenticate.setOnClickListener {
            val fullName = etFullName.text.toString()
            val dateOfBirth = etDateOfBirth.text.toString()
            if (fullName.length >= 3 && Utils.validDate(dateOfBirth)) {
                lifecycleScope.launch {
                    val success = Api.authenticate(
                        applicationContext,
                        fullName = fullName,
                        dateOfBirth = dateOfBirth,
                        fcmToken = Storage.getFCMToken(applicationContext),
                    )
                    if (success) {
                        Storage.setFullName(applicationContext, fullName = fullName)
                        Storage.setDateOfBirth(applicationContext, dateOfBirth = dateOfBirth)
                        Utils.toast(applicationContext, getString(R.string.auth_success))
                        setResult(0)
                        finish()
                    } else {
                        Utils.toast(applicationContext, getString(R.string.auth_failure))
                        setResult(1)
                        finish()
                    }
                }
            }
        }
    }
}