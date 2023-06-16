package com.speer.technologies.view.impl.fragment.searchuser.fragment

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.speer.technologies.R
import com.speer.technologies.databinding.FragmentSearchUserBinding
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.presentation.impl.connections.model.FetchMode
import com.speer.technologies.presentation.impl.searchusers.model.UserState
import com.speer.technologies.presentation.impl.searchusers.viewmodel.SearchUserViewModel
import com.speer.technologies.utils.extensions.common.EMPTY
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import com.speer.technologies.view.base.BaseFragment
import com.speer.technologies.view.impl.common.user.mapper.UserToParcelableUserMapper
import com.speer.technologies.view.impl.common.userprofile.hide
import com.speer.technologies.view.impl.common.userprofile.show
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
                    TransitionManager.beginDelayedTransition(root)
                    when (it) {
                        UserState.Empty -> {
                            layoutUserInfo.hide()
                            emptyUsersTv.visibility = View.VISIBLE
                            emptyUsersTv.setText(R.string.start_typing_username)
                        }

                        UserState.NotFound -> {
                            layoutUserInfo.hide()
                            emptyUsersTv.visibility = View.VISIBLE
                            emptyUsersTv.setText(R.string.not_found)
                        }

                        is UserState.Found -> {
                            emptyUsersTv.visibility = View.GONE
                            binding.layoutUserInfo.show(
                                user = it.user,
                                onFollowersClickListener = {
                                    openConnectionsScreen(it, FetchMode.FOLLOWERS)
                                },
                                onFollowingClickListener = {
                                    openConnectionsScreen(it, FetchMode.FOLLOWING)
                                },
                            )
                        }
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
}
