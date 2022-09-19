package com.farhanryanda.submission3githubuser.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhanryanda.submission3githubuser.api.ApiConfig
import com.farhanryanda.submission3githubuser.data.model.UsersItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    companion object {
        private const val TAG = "FollowerFragment"
    }

    val userListFollower = MutableLiveData<ArrayList<UsersItem>>()

    fun setUserFollowers(user: String) {
        val client = ApiConfig.getApiService().getFollowers(user)
        client.enqueue(object : Callback<ArrayList<UsersItem>> {
            override fun onResponse(
                call: Call<ArrayList<UsersItem>>,
                response: Response<ArrayList<UsersItem>>
            ) {
                val responseBody = response.body()
                userListFollower.postValue(responseBody)
            }

            override fun onFailure(call: Call<ArrayList<UsersItem>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getUserFollowers(): LiveData<ArrayList<UsersItem>> {
        return userListFollower
    }
}