package com.toyibnurseha.dicodingsubmission.ui.base

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.toyibnurseha.dicodingsubmission.R
import com.toyibnurseha.dicodingsubmission.data.UserInfo
import com.toyibnurseha.dicodingsubmission.ui.main.UserDetailActivity
import kotlinx.android.synthetic.main.card_search.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val userData = ArrayList<UserInfo>()

    companion object {
        const val DATA = "userData"
    }

    fun setData(items: ArrayList<UserInfo>) {
        userData.clear()
        userData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_search, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = userData.size

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.bind(userData[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userItems: UserInfo) {
            with(itemView) {
                tv_username.text = userItems.username

                Glide.with(itemView.context).load(userItems.avatar)
                    .apply(RequestOptions().override(50, 50)).into(iv_user)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, UserDetailActivity::class.java)
                    intent.putExtra(DATA, userItems)
                    context.startActivity(intent)
                }
            }
        }
    }
}