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

class FollowingViewModel : ViewModel() {
    companion object {
        private const val TAG = "FollowingFragment"
    }

    val userListFollowing = MutableLiveData<ArrayList<UsersItem>>()

    fun setUserFollowing(user: String) {
        val client = ApiConfig.getApiService().getFollowing(user)
        client.enqueue(object : Callback<ArrayList<UsersItem>> {
            override fun onResponse(
                call: Call<ArrayList<UsersItem>>,
                response: Response<ArrayList<UsersItem>>
            ) {
                val responseBody = response.body()
                userListFollowing.postValue(responseBody)
            }

            override fun onFailure(call: Call<ArrayList<UsersItem>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getUserFollowing(): LiveData<ArrayList<UsersItem>> {
        return userListFollowing
    }
}