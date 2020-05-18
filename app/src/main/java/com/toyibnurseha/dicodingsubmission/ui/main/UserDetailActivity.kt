package com.toyibnurseha.dicodingsubmission.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.toyibnurseha.dicodingsubmission.R
import com.toyibnurseha.dicodingsubmission.data.UserInfo
import com.toyibnurseha.dicodingsubmission.data.UserViewModel
import com.toyibnurseha.dicodingsubmission.ui.base.TabsPagerAdapter
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.app_header.*

class UserDetailActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var userViewModel: UserViewModel

    companion object {
        const val DATA = "userData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        getUser()
        toastMessage()
    }

    private fun init() {
        setContentView(R.layout.activity_user_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val pagerAdapter =
            TabsPagerAdapter(
                this,
                supportFragmentManager
            )
        view_pager_detail.adapter = pagerAdapter
        tab_detail.setupWithViewPager(view_pager_detail)
    }

    fun getUser(): String {
        val userInfo = intent.getParcelableExtra<UserInfo>(DATA)
        username = userInfo?.username.toString()

        showLoading(true)

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        userViewModel.getUserDetail().observe(this, Observer { userItems ->
            if (userItems != null) {
                showLoading(false)
                tv_username.text = userItems.username
                tv_name.text = userItems.name
                tv_bio.text = userItems.bio
                Glide.with(this).load(userItems.avatar).into(iv_profile)
            } else {
                showLoading(false)
                Toast.makeText(this, "forbidden", Toast.LENGTH_SHORT).show()
            }
        })

        userViewModel.setUserDetail(username)

        return username
    }

    private fun toastMessage() {
        userViewModel.getToastMessage().observe(this, Observer { message ->
            showLoading(false)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val intentSearch = Intent(this, MainActivity::class.java)
                startActivity(intentSearch)
            }
            R.id.language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

}
