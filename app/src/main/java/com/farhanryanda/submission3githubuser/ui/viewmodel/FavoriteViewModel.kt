package com.farhanryanda.submission3githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.farhanryanda.submission3githubuser.data.local.FavoriteEntity
import com.farhanryanda.submission3githubuser.data.local.FavoriteUserDao
import com.farhanryanda.submission3githubuser.data.local.UsersDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDatabase: UsersDatabase?

    init {
        userDatabase = UsersDatabase.getDatabase(application)
        userDao = userDatabase?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteEntity>>? {
        return userDao?.getFavoritedUser()
    }
}