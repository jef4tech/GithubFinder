package com.jef4tech.githubfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jef4tech.githubfinder.databinding.AdapterUsersBinding
import com.jef4tech.githubfinder.models.UserResponse
import com.jef4tech.githubfinder.utils.Extensions

/**
 * @author jeffin
 * @date 23/01/23
 */
class UserAdapter(private val listener: (user: UserResponse.Item) -> Unit): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
//    private var userList:List<UserResponse> = ArrayList()
//    private var listData = ArrayList<UserResponse.Item>()
    inner class UserViewHolder(val custombind:AdapterUsersBinding):RecyclerView.ViewHolder(custombind.root)

    private val differCallback = object : DiffUtil.ItemCallback<UserResponse.Item>() {
        override fun areItemsTheSame(oldItem: UserResponse.Item, newItem: UserResponse.Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserResponse.Item, newItem: UserResponse.Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemBinding = AdapterUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = differ.currentList[position]
        holder.custombind.apply {
            tvName.text = user.login
            layout1.setOnClickListener{
                listener.invoke(user)
            }
        }
        Extensions.loadImagefromUrl(holder.custombind.ivUser.context,holder.custombind.ivUser,user.avatarUrl)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
   private fun deleteAll(){
       differ.currentList.clear()
    }

}