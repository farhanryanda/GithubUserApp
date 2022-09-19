package com.farhanryanda.submission3githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farhanryanda.submission3githubuser.data.model.UsersItem
import com.farhanryanda.submission3githubuser.databinding.ItemUserGitBinding

class UserGitAdapter : RecyclerView.Adapter<UserGitAdapter.ListViewHolder>() {

    private val userList = ArrayList<UsersItem>()

    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun setListUser(user: ArrayList<UsersItem>) {
        userList.clear()
        userList.addAll(user)
        notifyDataSetChanged()
    }

    class ListViewHolder(val binding: ItemUserGitBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserGitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, type, avatar,username ) = userList[position]
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.binding.imgUserPhoto)
        holder.binding.tvUsername.text = username
        holder.binding.tvType.text = type
        holder.binding.tvId.text = id.toString()

        holder.itemView.setOnClickListener {
            onItemClickCallBack.onItemClicked(userList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = userList.size

    interface OnItemClickCallBack {
        fun onItemClicked(data: UsersItem)
    }
}