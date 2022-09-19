package com.farhanryanda.submission3githubuser.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.farhanryanda.submission3githubuser.api.ApiConfig
import com.farhanryanda.submission3githubuser.data.local.FavoriteEntity
import com.farhanryanda.submission3githubuser.data.local.FavoriteUserDao
import com.farhanryanda.submission3githubuser.data.local.UsersDatabase
import com.farhanryanda.submission3githubuser.data.model.ResponseUserDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "UserDetailActivity"
    }

    val userList = MutableLiveData<ResponseUserDetail>()

    private var userDao: FavoriteUserDao?
    private var userDatabase: UsersDatabase?

    init {
        userDatabase = UsersDatabase.getDatabase(application)
        userDao = userDatabase?.favoriteUserDao()
    }

    fun setUserDetail(user: String) {
        val client = ApiConfig.getApiService().getUserDetail(user)
        client.enqueue(object : Callback<ResponseUserDetail> {
            override fun onResponse(
                call: Call<ResponseUserDetail>,
                response: Response<ResponseUserDetail>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    userList.postValue(responseBody!!)
                }
            }

            override fun onFailure(call: Call<ResponseUserDetail>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message} ")
            }

        })
    }

    fun getUserDetail(): LiveData<ResponseUserDetail> {
        return userList
    }

    fun addToFavorite(id: Int, type: String, avatarUrl: String,login: String ) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteEntity(
                id,
                type,
                avatarUrl,
                login
            )
            userDao?.insertToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }

}