package com.toyibnurseha.dicodingsubmission.ui.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.toyibnurseha.dicodingsubmission.R
import com.toyibnurseha.dicodingsubmission.data.SearchViewModel
import com.toyibnurseha.dicodingsubmission.ui.base.UserAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var userViewModel: SearchViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        getUserList()
    }

    private fun init() {
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }

    private fun getUserList() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rv_user_list.adapter = adapter
        rv_user_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SearchViewModel::class.java)

        showLoading(false)

        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
            }
            false
        }

        userViewModel.getUserList().observe(this, Observer { userItems ->
            if (userItems != null && userItems.size != 0) {
                adapter.setData(userItems)
                showLoading(false)
                tv_not_found.visibility = View.INVISIBLE
            } else {
                //still set data so the data will be refresh with an empty list
                adapter.setData(userItems)
                showLoading(false)
                tv_not_found.visibility = View.VISIBLE
            }
        })

    }

    private fun doSearch() {
        val username = et_search.text.toString()
        if (username.isEmpty()) {
            showLoading(false)
            et_search.error = resources.getString(R.string.emptyText)
        } else {
            showLoading(true)
            userViewModel.setUser(username)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}
