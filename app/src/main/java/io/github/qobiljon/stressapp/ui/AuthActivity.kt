package io.github.qobiljon.stressapp.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.api.ApiHelper
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.utils.Utils
import kotlinx.coroutines.launch


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val tvFullName = findViewById<TextView>(R.id.tvFullName)
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val spGender = findViewById<Spinner>(R.id.spGender)
        val tvDateOfBirth = findViewById<TextView>(R.id.tvDateOfBirth)
        val etDateOfBirth = findViewById<EditText>(R.id.etDateOfBirth)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvRePassword = findViewById<TextView>(R.id.tvRepeatPassword)
        val etRePassword = findViewById<EditText>(R.id.etRepeatPassword)
        val tvSignInPrompt = findViewById<TextView>(R.id.tvSignInPrompt)
        val llSignInPrompt = findViewById<LinearLayout>(R.id.llSignInPrompt)
        val tvSignUpPrompt = findViewById<TextView>(R.id.tvSignUpPrompt)
        val llSignUpPrompt = findViewById<LinearLayout>(R.id.llSignUpPrompt)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val showSignInScene = {
            listOf(tvFullName, etFullName, tvGender, spGender, tvDateOfBirth, etDateOfBirth, tvRePassword, etRePassword, llSignInPrompt, btnSignUp).forEach {
                it.visibility = View.GONE
            }
            listOf(btnSignIn, llSignUpPrompt).forEach {
                it.visibility = View.VISIBLE
            }
        }
        val showSignUpScene = {
            listOf(tvFullName, etFullName, tvGender, spGender, tvDateOfBirth, etDateOfBirth, tvRePassword, etRePassword, llSignInPrompt, btnSignUp).forEach {
                it.visibility = View.VISIBLE
            }
            listOf(btnSignIn, llSignUpPrompt).forEach {
                it.visibility = View.GONE
            }
        }

        ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spGender.adapter = it
            spGender.setSelection(0)
        }

        tvSignInPrompt.setOnClickListener { showSignInScene() }
        tvSignUpPrompt.setOnClickListener { showSignUpScene() }

        btnSignUp.setOnClickListener {
            if (etPassword.text.toString() != etRePassword.text.toString()) {
                Utils.toast(applicationContext, getString(R.string.passwords_dont_match))
                return@setOnClickListener
            }

            btnSignUp.isEnabled = false
            lifecycleScope.launch {
                val success = ApiHelper.signUp(
                    applicationContext,
                    email = etEmail.text.toString(),
                    fullName = etFullName.text.toString(),
                    gender = if ((spGender.selectedItem as String).equals("남자")) "M" else "F",
                    dateOfBirth = etDateOfBirth.text.toString(),
                    fcmToken = DatabaseHelper.getFcmToken(applicationContext),
                    password = etPassword.text.toString(),
                )
                if (success) {
                    Utils.toast(applicationContext, getString(R.string.sign_up_success))

                    btnSignUp.isEnabled = true
                    showSignInScene()
                } else {
                    Utils.toast(applicationContext, getString(R.string.sign_up_failure))
                    btnSignUp.isEnabled = true
                    setResult(1)
                }
            }
        }
        btnSignIn.setOnClickListener {
            btnSignIn.isEnabled = false
            lifecycleScope.launch {
                val success = ApiHelper.signIn(
                    applicationContext,
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString(),
                )
                if (success) {
                    Utils.toast(applicationContext, getString(R.string.sign_in_success))

                    if (DatabaseHelper.hasFcmToken(applicationContext)) ApiHelper.setFcmToken(
                        applicationContext,
                        token = DatabaseHelper.getAuthToken(applicationContext),
                        fcmToken = DatabaseHelper.getFcmToken(applicationContext),
                    )

                    btnSignIn.isEnabled = true
                    setResult(12)
                    finish()
                } else {
                    Utils.toast(applicationContext, getString(R.string.sign_in_failure))
                    btnSignIn.isEnabled = true
                    setResult(24)
                }
            }
        }
    }
}
