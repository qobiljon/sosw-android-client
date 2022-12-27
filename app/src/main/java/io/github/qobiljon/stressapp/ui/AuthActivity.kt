package io.github.qobiljon.stressapp.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.api.ApiHelper
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.databinding.ActivityAuthBinding
import io.github.qobiljon.stressapp.utils.Utils
import kotlinx.coroutines.launch


class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_auth)
        setupView()

        ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spGender.adapter = it
            binding.spGender.setSelection(0)
        }

        binding.tvSignInPrompt.setOnClickListener { showSignInScene() }
        binding.tvSignUpPrompt.setOnClickListener { showSignUpScene() }
    }

    private fun setupView() {
        signIn()
        signUp()
    }

    private fun signUp() {
        with(binding.btnSignUp) {
            setOnClickListener {
                if (binding.etPassword.text.toString() != binding.etRepeatPassword.text.toString()) {
                    Utils.toast(applicationContext, getString(R.string.passwords_dont_match))
                    return@setOnClickListener
                }

                isEnabled = false
                lifecycleScope.launch {
                    val success = ApiHelper.signUp(
                        applicationContext,
                        email = binding.etEmail.text.toString(),
                        fullName = binding.etFullName.text.toString(),
                        gender = if ((binding.spGender.selectedItem as String) == "남자") "M" else "F",
                        dateOfBirth = binding.etDateOfBirth.text.toString(),
                        fcmToken = DatabaseHelper.getFcmToken(applicationContext),
                        password = binding.etPassword.text.toString(),
                    )
                    if (success) {
                        Utils.toast(applicationContext, getString(R.string.sign_up_success))

                        isEnabled = true
                        showSignInScene()
                    } else {
                        Utils.toast(applicationContext, getString(R.string.sign_up_failure))
                        isEnabled = true
                        setResult(1)
                    }
                }
            }
        }

    }

    private fun signIn() {
        with(binding.btnSignIn) {
            setOnClickListener {
                isEnabled = false
                lifecycleScope.launch {
                    val success = ApiHelper.signIn(
                        applicationContext,
                        email = binding.etEmail.text.toString(),
                        password = binding.etPassword.text.toString(),
                    )
                    if (success) {
                        Utils.toast(applicationContext, getString(R.string.sign_in_success))

                        if (DatabaseHelper.hasFcmToken(applicationContext)) ApiHelper.setFcmToken(
                            applicationContext,
                            token = DatabaseHelper.getAuthToken(applicationContext),
                            fcmToken = DatabaseHelper.getFcmToken(applicationContext),
                        )

                        isEnabled = true
                        setResult(12)
                        finish()
                    } else {
                        Utils.toast(applicationContext, getString(R.string.sign_in_failure))
                        isEnabled = true
                        setResult(24)
                    }
                }
            }
        }

    }

    private val showSignInScene = {
        with(binding) {
            listOf(
                tvFullName,
                etFullName,
                tvGender,
                spGender,
                tvDateOfBirth,
                etDateOfBirth,
                tvRepeatPassword,
                etRepeatPassword,
                llSignInPrompt,
                btnSignUp
            ).forEach {
                it.visibility = View.GONE
            }
            listOf(btnSignIn, llSignUpPrompt).forEach {
                it.visibility = View.VISIBLE
            }
        }
    }

    private val showSignUpScene = {
        with(binding) {
            listOf(
                tvFullName,
                etFullName,
                tvGender,
                spGender,
                tvDateOfBirth,
                etDateOfBirth,
                tvRepeatPassword,
                etRepeatPassword,
                llSignInPrompt,
                btnSignUp
            ).forEach {
                it.visibility = View.VISIBLE
            }
            listOf(btnSignIn, llSignUpPrompt).forEach {
                it.visibility = View.GONE
            }
        }
    }
}
