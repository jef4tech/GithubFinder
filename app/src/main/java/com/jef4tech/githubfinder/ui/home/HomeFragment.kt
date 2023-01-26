package com.jef4tech.githubfinder.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnCloseListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jef4tech.githubfinder.R
import com.jef4tech.githubfinder.adapters.UserAdapter
import com.jef4tech.githubfinder.databinding.FragmentHomeBinding
import com.jef4tech.githubfinder.models.UserResponse
import com.jef4tech.githubfinder.utils.Extensions

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var userAdapter: UserAdapter
    private var page = 1
    private  var searchKeyword =""
    private var isLoading = false
    private var limit = 10
    private lateinit  var homeViewModel:HomeViewModel



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        homeViewModel.getUserList()
        homeViewModel.userList.observe(viewLifecycleOwner) {
            userAdapter.setData(it.items,isLoading)
            Log.i("important", "onCreateView: $it")
        }
        //search listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(searchWord: String?): Boolean {
                searchKeyword=searchWord!!
                isLoading = false
                page = 1
                homeViewModel.getUserList(searchWord!!,1)
                return true
            }
        })
        binding.searchView.setOnCloseListener(object :OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {

            override fun onClose(): Boolean {
                // Clear the query text
            binding.searchView.setQuery("", false)
                // Clear the focus from the SearchView
            binding.searchView.clearFocus()
             userAdapter.clearAllData()
                return true
            }
        })
        setupRecyclerView()
        return root
    }

    private fun setupRecyclerView()=binding.userRecyclerView.apply {
        userAdapter= UserAdapter{position -> onClick(position)}
        adapter=userAdapter
        layoutManager= LinearLayoutManager(context)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = (layoutManager as LinearLayoutManager).childCount
                val totalItemCount = (layoutManager as LinearLayoutManager).itemCount
                val firstVisibleItemPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                    Log.i("positionscroll", "onScrolled:${userAdapter.getVariableValue()} ")
                    Log.i("positionscroll1", "limit:${limit} ")
                    // make API call to get the next set of users
                    if (userAdapter.getVariableValue()==limit){
                 apiCall()
                    }
                }
            }
        })
    }

    private fun apiCall() {
        page++
        limit += 10
        isLoading = true
        Log.i("scrolling end", "onScrolled: $page")
        homeViewModel.getUserList(searchKeyword, page)
    }

    private fun onClick(position: UserResponse.Item) {

        val bundle= Bundle()
        bundle.putString("username", position.login)
        view?.let { Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_navigation_dashboard,bundle) }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}