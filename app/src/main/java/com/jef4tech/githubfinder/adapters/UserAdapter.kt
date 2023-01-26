package com.jef4tech.githubfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
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
    private var listData = ArrayList<UserResponse.Item>()
    inner class UserViewHolder(val custombind:AdapterUsersBinding):RecyclerView.ViewHolder(custombind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemBinding = AdapterUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = listData[position]
        holder.custombind.apply {
            tvName.text = user.login
            layout1.setOnClickListener{
                listener.invoke(user)
            }
        }
        Extensions.loadImagefromUrl(holder.custombind.ivUser.context,holder.custombind.ivUser,user.avatarUrl)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(newListData: List<UserResponse.Item>, isLoading: Boolean){
        if (newListData == null) return
        if (!isLoading){
        listData.clear()
        }
        listData.addAll(newListData)
        notifyDataSetChanged()
    }
    fun getVariableValue() : Int {
        return listData.size
    }

    fun clearAllData(){
        listData.clear()
        notifyDataSetChanged()
    }
}