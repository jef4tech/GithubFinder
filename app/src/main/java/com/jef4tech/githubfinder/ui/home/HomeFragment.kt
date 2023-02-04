package com.jef4tech.githubfinder.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var userAdapter: UserAdapter
    private var searchKeyword =""
    private lateinit  var homeViewModel:HomeViewModel
    val QUERY_PAGE_SIZE = 20


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
        homeViewModel.userList.observe(viewLifecycleOwner) {
            userAdapter.differ.submitList(it.items)
            userAdapter.notifyDataSetChanged()
            Log.i("important", "onCreateView: $it")
        }

        var job: Job? = null
        //search listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(searchWord: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    searchKeyword=searchWord!!
                    homeViewModel.apply {
                        isSearched = false
                        userListPage = 1
                        getUserList(searchWord)
                    }
                }
                return true
            }
        })

        setupRecyclerView()
        return root
    }



    private fun setupRecyclerView(){
        userAdapter= UserAdapter{position -> onClick(position)}
        binding.userRecyclerView.apply {
            adapter=userAdapter
            layoutManager= LinearLayoutManager(context)
            addOnScrollListener(this@HomeFragment.scrollListener)
        }
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
    var is_Loading = true
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !is_Loading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
//            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
//            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
//                    isTotalMoreThanVisible && isScrolling
            if(isAtLastItem) {
                val userListSize = homeViewModel.userList.value?.items?.size
                if (userListSize==homeViewModel.limit){
                    homeViewModel.limit +=10
                    Extensions.showToast("Load more",context!!)
                    homeViewModel.apply {
                    getUserList(searchKeyword)
                    isSearched = true
                }
                isScrolling = false
            }
            }
            }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

}