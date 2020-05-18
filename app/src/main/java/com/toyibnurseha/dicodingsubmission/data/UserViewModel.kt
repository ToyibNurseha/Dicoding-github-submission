package com.toyibnurseha.dicodingsubmission.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class UserViewModel : ViewModel() {

    private val apiKey = "token 3fb52402ff897f4ceb4781adbd5222bbd4481925"
    val userDetail = MutableLiveData<UserInfo>()
    val userFollowers = MutableLiveData<ArrayList<UserInfo>>()
    private val toastMessage = MutableLiveData<String>()

    companion object {
        const val FAIL = "failed"
        const val TOAST = "error message: "
    }

    fun setUserDetail(username: String) {
        val url = "https://api.github.com/users/$username"

        AndroidNetworking.get(url)
            .setPriority(Priority.MEDIUM)
            .addHeaders("Authorization", apiKey)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    try {
                        val userInfo = UserInfo()

                        if (response != null) {
                            userInfo.name = response.optString("name")
                            userInfo.username = response.optString("login")
                            userInfo.bio = response.optString("bio")
                            userInfo.avatar = response.optString("avatar_url")
                            userDetail.postValue(userInfo)
                        }
                    }catch (e: Exception){
                        Log.d(FAIL, e.message.toString())
                        setToastMessage(e.message.toString())
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d(FAIL, anError?.message.toString())
                    setToastMessage(anError?.message.toString())
                }

            })
    }

    fun getUserDetail(): LiveData<UserInfo> {
        return userDetail
    }

    fun setFollowers(username: String, followersParam: String) {

        val followers = ArrayList<UserInfo>()
        val url = "https://api.github.com/users/$username/$followersParam"


            AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", apiKey)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener{
                    override fun onResponse(response: JSONArray?) {

                        try {
                            if (response != null) {
                                for (i in 0 until response.length()) {
                                    val follower = response.getJSONObject(i)
                                    val followerItems = UserInfo()

                                    followerItems.username = follower.optString("login")
                                    followerItems.avatar = follower.optString("avatar_url")

                                    followers.add(followerItems)
                                }
                                userFollowers.postValue(followers)
                            }
                        }catch (e: Exception){
                            Log.d(FAIL, e.message.toString())
                        }
                    }


                    override fun onError(anError: ANError?) {
                        Log.d(FAIL, anError?.message.toString())
                        setToastMessage(anError?.message.toString())
                    }

                })

    }

    fun getFollowers(): LiveData<ArrayList<UserInfo>> {
        return userFollowers
    }

    fun setToastMessage(message: String) {
        toastMessage.value = TOAST + message
    }

    fun getToastMessage(): LiveData<String> {
        return toastMessage
    }
}