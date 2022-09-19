package com.farhanryanda.submission3githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhanryanda.submission3githubuser.R
import com.farhanryanda.submission3githubuser.adapter.UserGitAdapter
import com.farhanryanda.submission3githubuser.data.model.UsersItem
import com.farhanryanda.submission3githubuser.databinding.ActivityHomeBinding
import com.farhanryanda.submission3githubuser.ui.setting.SettingPreferences
import com.farhanryanda.submission3githubuser.ui.setting.SettingViewModel
import com.farhanryanda.submission3githubuser.ui.setting.ViewModelFactory
import com.farhanryanda.submission3githubuser.ui.viewmodel.HomeViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: UserGitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)

        setRecycleList()

        searchUser()

        viewModel.getSearchUsers().observe(this@HomeActivity) {
            it?.let {
                adapter.setListUser(it)
                showLoading(false)
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

    private fun searchUser() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String): Boolean {
                if (q.isEmpty()) {
                    return true
                } else {
                    viewModel.setSearchUsers(q)
                    showLoading(true)
                }
                return true
            }

            override fun onQueryTextChange(q: String?): Boolean {
                return false
            }
        })
    }

    private fun setRecycleList() {
        adapter = UserGitAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserGitAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: UsersItem) {
                showSelectedUserGit(data)
            }
        })

        binding.rvHome.layoutManager = LinearLayoutManager(this@HomeActivity)
        binding.rvHome.setHasFixedSize(true)
        binding.rvHome.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showSelectedUserGit(userGit: UsersItem) {
        val moveWithObjectIntent = Intent(this@HomeActivity, UserDetailActivity::class.java)
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
                Intent(this,FavoriteActivity::class.java).also { intent ->
                    startActivity(intent)
                }
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