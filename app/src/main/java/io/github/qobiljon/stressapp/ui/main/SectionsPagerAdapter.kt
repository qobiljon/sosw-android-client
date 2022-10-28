package io.github.qobiljon.stressapp.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.github.qobiljon.stressapp.R

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if (position == 0) return DashboardFragment()
        return SelfReportFragment()
    }

    override fun getPageTitle(position: Int): CharSequence {
        if (position == 0) return context.getString(R.string.tab_dashboard)
        return context.getString(R.string.tab_selfreport)
    }

    override fun getCount(): Int {
        return 2
    }
}