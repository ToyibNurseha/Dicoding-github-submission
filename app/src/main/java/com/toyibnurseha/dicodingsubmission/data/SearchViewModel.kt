package com.toyibnurseha.dicodingsubmission.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject

class SearchViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<UserInfo>>()

    companion object {
        const val FAIL = "failed"
    }

    fun setUser(username: String) {

        val userDetail = ArrayList<UserInfo>()
        val apiKey = "token 3fb52402ff897f4ceb4781adbd5222bbd4481925"
        val url = "https://api.github.com/search/users?q=$username"

        AndroidNetworking.get(url)
            .setPriority(Priority.MEDIUM)
            .addHeaders("Authorization", apiKey)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val items = response?.getJSONArray("items")

                        if (items != null) {
                            for (i in 0 until items.length()) {
                                val user = items.getJSONObject(i)
                                val userItems = UserInfo()

                                userItems.username = user.optString("login")
                                userItems.avatar = user.optString("avatar_url")

                                userDetail.add(userItems)
                            }
                            listUser.postValue(userDetail)
                        }
                    } catch (e: Exception) {
                        Log.d(FAIL, e.message.toString())
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d(FAIL, anError?.message.toString())
                }

            })
    }

    fun getUserList(): LiveData<ArrayList<UserInfo>> {
        return listUser
    }

}