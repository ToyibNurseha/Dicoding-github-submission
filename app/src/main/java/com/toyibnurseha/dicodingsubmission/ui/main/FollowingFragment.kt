package com.toyibnurseha.dicodingsubmission.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.toyibnurseha.dicodingsubmission.R
import com.toyibnurseha.dicodingsubmission.data.UserViewModel
import com.toyibnurseha.dicodingsubmission.ui.base.UserAdapter
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var followerViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFollowers()
        setFollowers()
    }

    private fun getFollowers() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rv_follower.adapter = adapter
        rv_follower.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rv_follower.isNestedScrollingEnabled = false
        rv_follower.setHasFixedSize(true)

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UserViewModel::class.java
        )

        followerViewModel.getFollowers()
            .observe(this.viewLifecycleOwner, Observer { followerItems ->
                if (followerItems != null && followerItems.isNotEmpty()) {
                    adapter.setData(followerItems)
                } else {
                    no_follower.visibility = View.VISIBLE
                }
            })
    }

    private fun setFollowers() {
        val username = (activity as UserDetailActivity).getUser()
        followerViewModel.setFollowers(username, "following")
    }

}
