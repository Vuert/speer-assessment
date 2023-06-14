package com.speer.technologies.view.impl.fragment.searchUser.fragment

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.speer.technologies.R
import com.speer.technologies.databinding.FragmentSearchUserBinding
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.presentation.impl.connections.model.FetchMode
import com.speer.technologies.presentation.impl.searchUsers.model.UserState
import com.speer.technologies.presentation.impl.searchUsers.viewmodel.SearchUserViewModel
import com.speer.technologies.utils.extensions.common.EMPTY
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import com.speer.technologies.utils.view.ImageUtil
import com.speer.technologies.view.base.BaseFragment
import com.speer.technologies.view.impl.common.user.mapper.UserToParcelableUserMapper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

class SearchUserFragment : BaseFragment<FragmentSearchUserBinding, SearchUserViewModel>() {

    override fun onViewBound(binding: FragmentSearchUserBinding, savedInstanceState: Bundle?) {
        initSearchView(binding, viewModel)
        initUsersView(binding, viewModel)
    }

    private fun initSearchView(binding: FragmentSearchUserBinding, viewModel: SearchUserViewModel) {
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

    private fun initUsersView(binding: FragmentSearchUserBinding, viewModel: SearchUserViewModel) {
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
                        UserState.Empty -> {
                            layoutUserInfo.followersTv.setOnClickListener(null)
                            layoutUserInfo.followingsTv.setOnClickListener(null)
                            scrollView.visibility = View.GONE
                            emptyUsersTv.visibility = View.VISIBLE
                            emptyUsersTv.setText(R.string.start_typing_username)
                        }

                        UserState.NotFound -> {
                            layoutUserInfo.followersTv.setOnClickListener(null)
                            layoutUserInfo.followingsTv.setOnClickListener(null)
                            scrollView.visibility = View.GONE
                            emptyUsersTv.visibility = View.VISIBLE
                            emptyUsersTv.setText(R.string.not_found)
                        }

                        is UserState.Found -> {
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
            ImageUtil.displayAvatar(userAvatarImg, user.avatarUrl)
            nameTv.setTextOrGone(user.additionalInfo?.name)
            userNameTv.setTextOrGone(user.username)
            descriptionTv.setTextOrGone(user.additionalInfo?.description)

            user.additionalInfo?.followersCount.let { followersCount ->
                followersTv.setTextOrGone(
                    followersCount?.let { getString(R.string.patter_followers, it) }
                )

                if (followersCount != null && followersCount > 0) {
                    followersTv.setOnClickListener {
                        openConnectionsScreen(user, FetchMode.FOLLOWERS)
                    }
                }
            }

            user.additionalInfo?.followingCount.let { followingCount ->
                followingsTv.setTextOrGone(
                    followingCount?.let { getString(R.string.patter_following, it) }
                )

                if (followingCount != null && followingCount > 0) {
                    followingsTv.setOnClickListener {
                        openConnectionsScreen(user, FetchMode.FOLLOWING)
                    }
                }
            }
        }
    }

    private fun openConnectionsScreen(user: User, fetchMode: FetchMode) {
        val action = SearchUserFragmentDirections
            .actionUsersFragmentToConnectionsFragment(
                UserToParcelableUserMapper().map(user),
                fetchMode,
            )
        findNavController().navigate(action)
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
