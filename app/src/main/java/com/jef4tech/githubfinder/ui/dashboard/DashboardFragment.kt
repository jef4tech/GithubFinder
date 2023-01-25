package com.jef4tech.githubfinder.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jef4tech.githubfinder.adapters.RepoAdapter
import com.jef4tech.githubfinder.databinding.FragmentDashboardBinding
import com.jef4tech.githubfinder.models.RepositoryResponse
import com.jef4tech.githubfinder.utils.Extensions


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var repoAdapter: RepoAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val loginId= arguments?.getString("username")
        val root: View = binding.root

        dashboardViewModel.getUserData(loginId!!)
        dashboardViewModel.getUserRepo(loginId!!)
        dashboardViewModel.repoData.observe(viewLifecycleOwner) {
            repoAdapter.setData(it)
        }
        dashboardViewModel.userData.observe(viewLifecycleOwner) {
            binding.apply {
                tvUserName.text = it.login
                tvEmail.text = it.email
                tvLocation.text = it.location
                tvJoinDate.text = it.createdAt
                tvFollowers.text = it.followers.toString()+" Followers"
                tvFollowing.text = "Following "+it.following.toString()
                tvBiography.text = it.bio

            }
            Extensions.loadImagefromUrl(binding.ivUser.context,binding.ivUser,it.avatarUrl)
        }

        dashboardViewModel.sortedRepoData.observe(viewLifecycleOwner) {
            repoAdapter.setData(it)
        }
        //search listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(searchWord: String?): Boolean {
                dashboardViewModel.search(searchWord!!)
                return true
            }

        })
        setupRecyclerView()
        return root
    }
    private fun setupRecyclerView()=binding.repoRecyclerView.apply {
        repoAdapter= RepoAdapter{position -> onClick(position)}
        adapter=repoAdapter
        layoutManager= LinearLayoutManager(context)
        addItemDecoration(DividerItemDecoration(binding.repoRecyclerView.context,DividerItemDecoration.VERTICAL))

    }
    private fun onClick(position: RepositoryResponse.RepositoryResponseItem) {
        val implicit = Intent(Intent.ACTION_VIEW, Uri.parse(position.htmlUrl))
        startActivity(implicit)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}