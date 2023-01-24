package com.jef4tech.githubfinder.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jef4tech.githubfinder.databinding.FragmentDashboardBinding
import com.jef4tech.githubfinder.utils.Extensions

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

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
        dashboardViewModel.userData.observe(viewLifecycleOwner) {
            binding.apply {
                tvUserName.text = it.login
                tvEmail.text = it.email
                tvLocation.text = it.location
                tvJoinDate.text = it.createdAt
                tvFollowers.text = it.followers.toString()
                tvFollowing.text = it.following.toString()
                tvBiography.text = it.bio

            }
            Extensions.loadImagefromUrl(binding.ivUser.context,binding.ivUser,it.avatarUrl)

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}