package com.farhanryanda.submission3githubuser.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.farhanryanda.submission3githubuser.R
import com.farhanryanda.submission3githubuser.adapter.SectionsPagerAdapter
import com.farhanryanda.submission3githubuser.databinding.ActivityUserDetailBinding
import com.farhanryanda.submission3githubuser.ui.setting.SettingPreferences
import com.farhanryanda.submission3githubuser.ui.setting.SettingViewModel
import com.farhanryanda.submission3githubuser.ui.setting.ViewModelFactory
import com.farhanryanda.submission3githubuser.ui.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserDetailActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_DETAILUSER = "extra_detailuser"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_DETAILUSER)
        val id = intent.getIntExtra(EXTRA_ID,0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        val type = intent.getStringExtra(EXTRA_TYPE)

        val bundle = Bundle()
        bundle.putString(EXTRA_DETAILUSER, username)

        viewModel = ViewModelProvider(
            this
        ).get(DetailViewModel::class.java)

        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this, {
            if (it != null) {
                binding.tvDetailName.text = it.name
                binding.tvDetailUsername.text = it.login
                binding.tvDetailRepository.text = it.publicRepos.toString()
                binding.tvDetailCompany.text = it.company
                binding.tvDetailLocation.text = it.location
                binding.tvFollowers.text = it.followers.toString()
                binding.tvFollowing.text = it.following.toString()
                Glide.with(this@UserDetailActivity)
                    .load(it.avatarUrl)
                    .circleCrop()
                    .into(binding.imgUserPhoto)
            }
        })

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null){
                    if (count > 0){
                        binding.btnFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.btnFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.btnFavorite.setOnClickListener{
            _isChecked = !_isChecked
            if (_isChecked) {
                if (username != null) {
                    if (avatar != null) {
                        if (type != null){
                            viewModel.addToFavorite(id,type,avatar,username)
                        }
                    }
                }
            } else {
                viewModel.removeFromFavorite(id)
            }
            binding.btnFavorite.isChecked = _isChecked
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = binding.viewPage
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

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