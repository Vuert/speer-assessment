package com.speer.technologies.view.impl.fragment.users.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.speer.technologies.R
import com.speer.technologies.databinding.FragmentUsersBinding
import com.speer.technologies.domain.users.model.User
import com.speer.technologies.presentation.impl.users.model.UsersState
import com.speer.technologies.presentation.impl.users.viewmodel.UsersViewModel
import com.speer.technologies.utils.extensions.common.EMPTY
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import com.speer.technologies.view.base.BaseFragment
import com.speer.technologies.view.impl.fragment.users.adapter.UsersAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

class UsersFragment : BaseFragment<FragmentUsersBinding, UsersViewModel>() {

    override fun onViewBound(binding: FragmentUsersBinding, savedInstanceState: Bundle?) {
        initSearchView(binding, viewModel)
        initRecyclerView(binding, viewModel)
    }

    private fun initSearchView(binding: FragmentUsersBinding, viewModel: UsersViewModel) {
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

    private fun initRecyclerView(binding: FragmentUsersBinding, viewModel: UsersViewModel) {
        val adapter = UsersAdapter(
            resources = resources,
            onUserClickListener = ::onUserClicked,
            onFollowersClickListener = ::onFollowersClicked,
            onFollowingsClickListener = ::onFollowingsClicked,
        )

        binding.usersRv.adapter = adapter
        binding.usersRv.addItemDecoration(
            MaterialDividerItemDecoration(
                binding.root.context,
                LinearLayout.VERTICAL
            )
        )

        viewLifecycleOwner.repeatOnStarted {
            viewModel.users.collectLatest {
                when (it) {
                    UsersState.Empty -> {
                        binding.usersRv.visibility = View.GONE
                        binding.emptyUsersTv.visibility = View.VISIBLE
                        binding.emptyUsersTv.setText(R.string.start_typing_username)
                    }

                    is UsersState.Loaded -> {
                        if (it.list.isEmpty()) {
                            binding.usersRv.visibility = View.GONE
                            binding.emptyUsersTv.visibility = View.VISIBLE
                            binding.emptyUsersTv.setText(R.string.not_found)
                        } else {
                            binding.usersRv.visibility = View.VISIBLE
                            binding.emptyUsersTv.visibility = View.GONE
                        }
                        adapter.submitList(it.list)
                    }
                }
            }
        }
    }

    private fun onUserClicked(user: User) {
        // TODO add transition to the next screen
    }

    private fun onFollowersClicked(user: User) {
        // TODO add transition to the next screen
    }

    private fun onFollowingsClicked(user: User) {
        // TODO add transition to the next screen
    }
}
