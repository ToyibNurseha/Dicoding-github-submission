package com.toyibnurseha.dicodingsubmission.ui.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.toyibnurseha.dicodingsubmission.R
import com.toyibnurseha.dicodingsubmission.ui.main.FollowerFragment
import com.toyibnurseha.dicodingsubmission.ui.main.FollowingFragment

class TabsPagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val TITLES = intArrayOf(
        R.string.tab_follower,
        R.string.tab_following
    )

    override fun getItem(position: Int): Fragment {

        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowerFragment()
            }
            1 -> {
                fragment = FollowingFragment()
            }
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}