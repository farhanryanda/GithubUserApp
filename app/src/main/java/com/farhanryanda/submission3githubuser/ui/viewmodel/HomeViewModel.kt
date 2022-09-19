package com.farhanryanda.submission3githubuser.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhanryanda.submission3githubuser.api.ApiConfig
import com.farhanryanda.submission3githubuser.data.model.ResponseUser
import com.farhanryanda.submission3githubuser.data.model.UsersItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {

    companion object {
        private const val TAG = "HomeActivity"
    }

    val userList = MutableLiveData<ArrayList<UsersItem>>()

    fun setSearchUsers(user: String) {
        val client = ApiConfig.getApiService().getSearchUsers(user)
        client.enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    userList.postValue(responseBody?.items)
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message} ")
            }

        })
    }

    fun getSearchUsers(): LiveData<ArrayList<UsersItem>> {
        return userList
    }

}