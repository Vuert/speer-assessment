package com.speer.technologies.view.impl.fragment.users.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.speer.technologies.R
import com.speer.technologies.databinding.UserListItemBinding
import com.speer.technologies.domain.users.model.User
import com.speer.technologies.utils.view.DefaultItemCallback

class UsersAdapter(
    private val resources: Resources,
    private val onUserClickListener: (User) -> Unit,
    private val onFollowersClickListener: (User) -> Unit,
    private val onFollowingsClickListener: (User) -> Unit,
    itemCallback: DefaultItemCallback<User> = DefaultItemCallback(),
) : ListAdapter<User, UsersAdapter.UserViewHolder>(itemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindUser(getItem(position))
    }

    inner class UserViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onUserClickListener.invoke(getItem(adapterPosition))
            }
            binding.followersTv.setOnClickListener {
                onFollowersClickListener.invoke(getItem(adapterPosition))
            }
            binding.followingsTv.setOnClickListener {
                onFollowingsClickListener.invoke(getItem(adapterPosition))
            }
        }

        fun bindUser(user: User) {
            binding.apply {
                nameTv.text = user.name
                userNameTv.text = user.username
                descriptionTv.text = user.description
                followersTv.text = resources
                    .getString(R.string.patter_followers, user.followersCount)
                followingsTv.text = resources
                    .getString(R.string.patter_following, user.followingsCount)

                Glide
                    .with(userAvatarImg)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .error(R.drawable.baseline_person)
                    .placeholder(R.drawable.baseline_person)
                    .override(userAvatarImg.width, userAvatarImg.height)
                    .into(userAvatarImg)
            }
        }
    }
}
