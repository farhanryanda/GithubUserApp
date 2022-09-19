package com.farhanryanda.submission3githubuser.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhanryanda.submission3githubuser.R
import com.farhanryanda.submission3githubuser.adapter.UserGitAdapter
import com.farhanryanda.submission3githubuser.data.local.FavoriteEntity
import com.farhanryanda.submission3githubuser.data.model.UsersItem
import com.farhanryanda.submission3githubuser.databinding.ActivityFavoriteBinding
import com.farhanryanda.submission3githubuser.ui.setting.SettingPreferences
import com.farhanryanda.submission3githubuser.ui.setting.SettingViewModel
import com.farhanryanda.submission3githubuser.ui.setting.ViewModelFactory
import com.farhanryanda.submission3githubuser.ui.viewmodel.FavoriteViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: UserGitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserGitAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        setRecycleList()

        viewModel.getFavoriteUser()?.observe(this@FavoriteActivity) {
            if (it != null) {
                val listUser = mapListUser(it)
                adapter.setListUser(listUser)
            }
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSetting().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })
    }

    private fun mapListUser(favoriteUser: List<FavoriteEntity>): ArrayList<UsersItem> {
        val listUserFavorited = ArrayList<UsersItem>()
        for (user in favoriteUser) {
            val userMapped =  UsersItem (
                user.id,
                user.type!!,
                user.avatarUrl!!,
                user.login!!
                    )
            listUserFavorited.add(userMapped)
        }
        return listUserFavorited
    }

    private fun setRecycleList() {
        adapter = UserGitAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserGitAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: UsersItem) {
                showSelectedUserGit(data)
            }
        })

        binding.rvFavorited.layoutManager = LinearLayoutManager(this@FavoriteActivity)
        binding.rvFavorited.setHasFixedSize(true)
        binding.rvFavorited.adapter = adapter
    }

    private fun showSelectedUserGit(userGit: UsersItem) {
        val moveWithObjectIntent = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
        moveWithObjectIntent.putExtra(UserDetailActivity.EXTRA_DETAILUSER, userGit.login)
        moveWithObjectIntent.putExtra(UserDetailActivity.EXTRA_ID, userGit.id)
        moveWithObjectIntent.putExtra(UserDetailActivity.EXTRA_TYPE, userGit.type)
        moveWithObjectIntent.putExtra(UserDetailActivity.EXTRA_AVATAR,userGit.avatarUrl)
        startActivity(moveWithObjectIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite_menu -> {
                Toast.makeText(this,"Anda Sudah Berada Di Favorite",Toast.LENGTH_SHORT).show()
            }
            R.id.setting -> {
                Intent(this,SettingActivity::class.java).also { intent ->
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}