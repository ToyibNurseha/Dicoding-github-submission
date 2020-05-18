package com.toyibnurseha.dicodingsubmission.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class SearchViewModel : ViewModel() {

    val listUser = MutableLiveData<ArrayList<UserInfo>>()

    companion object {
        const val FAIL = "failed"
    }

    fun setUser(username: String) {

        val userDetail = ArrayList<UserInfo>()
        val apiKey = "token 3fb52402ff897f4ceb4781adbd5222bbd4481925"
        val url = "https://api.github.com/search/users?q=$username"
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
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val user = items.getJSONObject(i)
                        val userItems = UserInfo()

                        userItems.username = user.getString("login")
                        userItems.avatar = user.getString("avatar_url")

                        userDetail.add(userItems)
                    }

                    listUser.postValue(userDetail)
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

    fun getUserList(): LiveData<ArrayList<UserInfo>> {
        return listUser
    }

}