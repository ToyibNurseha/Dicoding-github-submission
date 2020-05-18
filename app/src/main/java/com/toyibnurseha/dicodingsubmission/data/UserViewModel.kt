package com.toyibnurseha.dicodingsubmission.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class UserViewModel : ViewModel() {

    private val apiKey = "token 3fb52402ff897f4ceb4781adbd5222bbd4481925"
    val userDetail = MutableLiveData<UserInfo>()
    val userFollowers = MutableLiveData<ArrayList<UserInfo>>()
    val toastMessage = MutableLiveData<String>()

    companion object {
        const val FAIL = "failed"
        const val TOAST = "error message : "
    }

    fun setUserDetail(username: String) {
        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                val responseObject = JSONObject(result)
                val userInfo = UserInfo()
                userInfo.username = responseObject.getString("login")
                userInfo.bio = responseObject.getString("bio")
                userInfo.avatar = responseObject.getString("avatar_url")
                userInfo.name = responseObject.getString("name")
                userDetail.postValue(userInfo)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(FAIL, error?.message.toString())
                setToastMessage(error?.message.toString())
            }
        })
    }

    fun getUserDetail(): LiveData<UserInfo> {
        return userDetail
    }

    fun setFollowers(username: String, followersParam: String) {

        val followers = ArrayList<UserInfo>()
        val url = "https://api.github.com/users/$username/$followersParam"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {

                    val result = String(responseBody!!)
                    val responseArray = JSONArray(result)

                    for (i in 0 until responseArray.length()) {
                        val follower = responseArray.getJSONObject(i)
                        val followerItems = UserInfo()

                        followerItems.username = follower.getString("login")
                        followerItems.avatar = follower.getString("avatar_url")
                        followers.add(followerItems)
                    }
                    userFollowers.postValue(followers)
                } catch (e: Exception) {
                    Log.d(FAIL, e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(FAIL, error?.message.toString())

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