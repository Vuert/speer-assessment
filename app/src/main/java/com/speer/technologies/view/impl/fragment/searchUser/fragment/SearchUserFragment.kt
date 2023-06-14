package com.speer.technologies.view.impl.fragment.searchUser.fragment

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.speer.technologies.R
import com.speer.technologies.databinding.FragmentSearchUserBinding
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.presentation.impl.users.model.UsersState
import com.speer.technologies.presentation.impl.users.viewmodel.UsersViewModel
import com.speer.technologies.utils.extensions.common.EMPTY
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import com.speer.technologies.view.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

class SearchUserFragment : BaseFragment<FragmentSearchUserBinding, UsersViewModel>() {

    override fun onViewBound(binding: FragmentSearchUserBinding, savedInstanceState: Bundle?) {
        initSearchView(binding, viewModel)
        initUsersView(binding, viewModel)
    }

    private fun initSearchView(binding: FragmentSearchUserBinding, viewModel: UsersViewModel) {
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.userName.value = newText ?: String.EMPTY
                    return true
                }
            }
        )
        viewLifecycleOwner.repeatOnStarted {
            viewModel
                .userName
                .filter { it != binding.searchView.query.toString() }
                .collectLatest {
                    binding.searchView.setQuery(it, false)
                }
        }
    }

    private fun initUsersView(binding: FragmentSearchUserBinding, viewModel: UsersViewModel) {

        binding.layoutUserInfo

        viewLifecycleOwner.repeatOnStarted {
            viewModel.isLoading.collectLatest {
                binding.progressBar.isVisible = it
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.user.collectLatest {
                binding.apply {
                    TransitionManager.beginDelayedTransition(root, AutoTransition())
                    when (it) {
                        UsersState.Empty -> {
                            layoutUserInfo.followersTv.setOnClickListener(null)
                            layoutUserInfo.followingsTv.setOnClickListener(null)
                            scrollView.visibility = View.GONE
                            emptyUsersTv.visibility = View.VISIBLE
                            emptyUsersTv.setText(R.string.start_typing_username)
                        }

                        UsersState.NotFound -> {
                            layoutUserInfo.followersTv.setOnClickListener(null)
                            layoutUserInfo.followingsTv.setOnClickListener(null)
                            scrollView.visibility = View.GONE
                            emptyUsersTv.visibility = View.VISIBLE
                            emptyUsersTv.setText(R.string.not_found)
                        }

                        is UsersState.Found -> {
                            scrollView.visibility = View.VISIBLE
                            emptyUsersTv.visibility = View.GONE
                            fillUserInfo(this, it.user)
                        }
                    }
                }
            }
        }
    }

    private fun fillUserInfo(binding: FragmentSearchUserBinding, user: User) {
        binding.layoutUserInfo.apply {
            nameTv.text = user.name
            userNameTv.setTextOrGone(user.username)
            descriptionTv.setTextOrGone(user.description)
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

            followersTv.setOnClickListener {
                // Transition to the next screen
            }

            followingsTv.setOnClickListener {
                // Transition to the next screen
            }
        }
    }

    private fun TextView.setTextOrGone(text: String?) {
        if (text.isNullOrEmpty()) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            this.text = text
        }
    }
}
