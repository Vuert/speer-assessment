package com.speer.technologies.view.impl.fragment.connections.fragment

import android.os.Bundle
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.speer.technologies.R
import com.speer.technologies.databinding.FragmentConnectionsBinding
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.presentation.impl.connections.model.FetchMode
import com.speer.technologies.presentation.impl.connections.viewmodel.ConnectionsViewModel
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import com.speer.technologies.view.base.BaseFragment
import com.speer.technologies.view.impl.common.user.mapper.ParcelableUserToUserMapper
import com.speer.technologies.view.impl.common.user.mapper.UserToParcelableUserMapper
import com.speer.technologies.view.impl.fragment.connections.adapter.ConnectionsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class ConnectionsFragment : BaseFragment<FragmentConnectionsBinding, ConnectionsViewModel>() {

    private val navArgs by navArgs<ConnectionsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.init(ParcelableUserToUserMapper().map(navArgs.user), navArgs.fetchMode)
        }
    }

    override fun onViewBound(binding: FragmentConnectionsBinding, savedInstanceState: Bundle?) {
        initToolbar(binding, viewModel)
        initConnectionsRecyclerView(binding, viewModel)
        initSwipeRefreshLayout(binding, viewModel)
    }

    private fun initToolbar(
        binding: FragmentConnectionsBinding,
        viewModel: ConnectionsViewModel,
    ) {
        binding.materialToolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        viewLifecycleOwner.repeatOnStarted {
            viewModel
                .user
                .map { it.user }
                .filterNotNull()
                .combine(viewModel.fetchMode) { user, fetchMode ->
                    when (fetchMode) {
                        FetchMode.FOLLOWERS -> getString(
                            R.string.title_followers,
                            user.username
                        )

                        FetchMode.FOLLOWING -> getString(
                            R.string.title_following,
                            user.username
                        )
                    }
                }
                .collectLatest(binding.materialToolbar::setTitle)
        }
    }

    private fun initConnectionsRecyclerView(
        binding: FragmentConnectionsBinding,
        viewModel: ConnectionsViewModel,
    ) {
        val adapter = ConnectionsAdapter(onUserClickListener = ::openConnectionsProfile)

        binding.connectionsRv.adapter = adapter
        binding.connectionsRv.addItemDecoration(
            MaterialDividerItemDecoration(
                binding.root.context,
                LinearLayout.VERTICAL,
            )
        )

        viewLifecycleOwner.repeatOnStarted {
            viewModel.connections.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    private fun openConnectionsProfile(user: User) {
        val action = ConnectionsFragmentDirections
            .actionConnectionsFragmentToConnectionProfileFragment(
                UserToParcelableUserMapper().map(user),
            )
        findNavController().navigate(action)
    }

    private fun initSwipeRefreshLayout(
        binding: FragmentConnectionsBinding,
        viewModel: ConnectionsViewModel
    ) {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.isLoading.collectLatest(binding.swipeRefresh::setRefreshing)
        }

        binding.swipeRefresh.setOnRefreshListener { viewModel.onRefresh() }
    }
}
