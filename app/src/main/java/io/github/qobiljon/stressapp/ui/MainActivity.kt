package io.github.qobiljon.stressapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.services.DataSubmissionService
import io.github.qobiljon.stressapp.ui.main.SectionsPagerAdapter
import io.github.qobiljon.stressapp.utils.Storage

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

    private fun runServices() {
        startForegroundService(Intent(applicationContext, DataSubmissionService::class.java))
    }
}