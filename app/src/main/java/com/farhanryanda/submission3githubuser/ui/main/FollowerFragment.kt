package com.farhanryanda.submission3githubuser.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhanryanda.submission3githubuser.R
import com.farhanryanda.submission3githubuser.adapter.UserGitAdapter
import com.farhanryanda.submission3githubuser.databinding.FragmentFollowerBinding
import com.farhanryanda.submission3githubuser.ui.viewmodel.FollowersViewModel

class FollowerFragment : Fragment(R.layout.fragment_follower) {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: UserGitAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        username = args?.getString(UserDetailActivity.EXTRA_DETAILUSER).toString()
        _binding = FragmentFollowerBinding.bind(view)

        setAdapter()

        showLoading(true)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)
        viewModel.setUserFollowers(username)
        viewModel.getUserFollowers().observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setListUser(it)
                showLoading(false)
            }
        })
    }

    private fun setAdapter() {
        adapter = UserGitAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFollower.setHasFixedSize(true)
        binding.rvFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvFollower.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFollower.visibility = View.VISIBLE
        } else {
            binding.progressBarFollower.visibility = View.GONE
        }
    }
}