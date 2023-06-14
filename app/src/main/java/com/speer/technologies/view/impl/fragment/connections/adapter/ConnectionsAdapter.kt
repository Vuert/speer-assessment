package com.speer.technologies.view.impl.fragment.connections.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.speer.technologies.databinding.ConnectionListItemBinding
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.utils.view.DefaultItemCallback
import com.speer.technologies.utils.view.ImageUtil

class ConnectionsAdapter(
    private val onUserClickListener: (User) -> Unit,
    itemCallback: DefaultItemCallback<User> = DefaultItemCallback(),
) : ListAdapter<User, ConnectionsAdapter.UserViewHolder>(itemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            ConnectionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindUser(getItem(position))
    }

    inner class UserViewHolder(private val binding: ConnectionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onUserClickListener.invoke(getItem(adapterPosition))
            }
        }

        fun bindUser(user: User) {
            binding.apply {
                userNameTv.text = user.username
                ImageUtil.displayAvatar(connectionAvatarImg, user.avatarUrl, circleCrop = true)
            }
        }
    }
}
