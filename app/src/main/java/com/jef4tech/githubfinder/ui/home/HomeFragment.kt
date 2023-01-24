package com.jef4tech.githubfinder.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jef4tech.githubfinder.R
import com.jef4tech.githubfinder.adapters.UserAdapter
import com.jef4tech.githubfinder.databinding.FragmentHomeBinding
import com.jef4tech.githubfinder.models.UserResponse
import kotlinx.coroutines.delay

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var userAdapter: UserAdapter



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        homeViewModel.getUserList()
        homeViewModel.userList.observe(viewLifecycleOwner) {
            userAdapter.setData(it.items)
            Log.i("important", "onCreateView: $it")
        }
        //search listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(searchWord: String?): Boolean {
                homeViewModel.getUserList(searchWord!!)
                return true
            }

        })

        setupUI()
        setupRecyclerView()
        return root
    }

    private fun setupRecyclerView()=binding.userRecyclerView.apply {
        userAdapter= UserAdapter{position -> onClick(position)}
        adapter=userAdapter
        layoutManager= LinearLayoutManager(context)
    }

    private fun onClick(position: UserResponse.Item) {

        val bundle= Bundle()
        bundle.putString("username", position.login)
//        bundle.putString("title", Id.title)
//        bundle.putString("description",Id.description)
//        bundle.putString("img",  Id.urlToImage)
//        bundle.putString("date",  Id.publishedAt)
//        bundle.putString("author",Id.author)
//        bundle.putString("content",Id.content)
//        bundle.putInt("playerId",Id)
        view?.let { Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_navigation_dashboard,bundle) }
    }

    private fun setupUI() {
//        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}