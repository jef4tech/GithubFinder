package com.jef4tech.githubfinder.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jef4tech.githubfinder.databinding.AdapterReposBinding
import com.jef4tech.githubfinder.databinding.AdapterUsersBinding
import com.jef4tech.githubfinder.models.RepositoryResponse
import com.jef4tech.githubfinder.models.UserResponse

/**
 * @author jeffin
 * @date 24/01/23
 */
class RepoAdapter(private val listener: (repo: RepositoryResponse.RepositoryResponseItem) -> Unit): RecyclerView.Adapter<RepoAdapter.UserViewHolder>() {
    private var listData = ArrayList<RepositoryResponse.RepositoryResponseItem>()
    inner class UserViewHolder(val custombind: AdapterReposBinding):RecyclerView.ViewHolder(custombind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemBinding = AdapterReposBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val repo = listData[position]
        holder.custombind.apply {
            tvRepoName.text = repo.name
            tvForks.text = repo.forksCount.toString()+"Forks"
            tvStars.text = repo.stargazersCount.toString()+"Stars"
        }
        holder.custombind.layout1.setOnClickListener {
            listener.invoke(repo)
        }
    }

    fun setData(newListData: List<RepositoryResponse.RepositoryResponseItem>){
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }
}