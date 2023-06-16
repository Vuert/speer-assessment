package com.speer.technologies.view.impl.fragment.connections.fragment

import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.speer.technologies.R
import com.speer.technologies.databinding.FragmentConnectionsBinding
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.presentation.impl.connections.model.FetchMode
import com.speer.technologies.presentation.impl.connections.viewmodel.ConnectionsViewModel
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import com.speer.technologies.utils.view.DefaultItemCallback
import com.speer.technologies.view.base.BaseFragment
import com.speer.technologies.view.impl.common.user.mapper.ParcelableUserToUserMapper
import com.speer.technologies.view.impl.common.user.mapper.UserToParcelableUserMapper
import com.speer.technologies.view.impl.fragment.connections.adapter.ConnectionsAdapter
import com.speer.technologies.view.impl.fragment.connections.adapter.LoadingIndicatorAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach

private const val ITEMS_AMOUNT_TO_LOAD_NEXT_PAGE = 4

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

        // Init title
        viewLifecycleOwner.repeatOnStarted {
            viewModel
                .user
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
        // Init adapters
        val connectionsAdapter = ConnectionsAdapter(
            onUserClickListener = ::openConnectionsProfile,
            itemCallback = DefaultItemCallback(
                areItemsTheSame = { oldItem, newItem ->
                    oldItem.id == newItem.id
                },
            ),
        )
        val loadingIndicatorAdapter = LoadingIndicatorAdapter()

        binding.connectionsRv.adapter = ConcatAdapter(
            connectionsAdapter,
            loadingIndicatorAdapter,
        )
        binding.connectionsRv.addItemDecoration(
            MaterialDividerItemDecoration(
                binding.root.context,
                LinearLayout.VERTICAL,
            )
        )

        // Init pagination
        var noMoreItems = false
        var isLoading = false

        val listBottomListener = object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (layoutManager != null &&
                    !isLoading &&
                    !noMoreItems &&
                    layoutManager.findLastCompletelyVisibleItemPosition() ==
                    connectionsAdapter.itemCount - ITEMS_AMOUNT_TO_LOAD_NEXT_PAGE - 1
                ) {
                    viewModel.onListBottomReached()
                }
            }
        }
        binding.connectionsRv.addOnScrollListener(listBottomListener)

        // Listen values
        viewLifecycleOwner.repeatOnStarted {
            viewModel
                .connections
                .onEach { binding.emptyTv.isVisible = it.isEmpty() }
                .collectLatest(connectionsAdapter::submitList)
        }
        viewLifecycleOwner.repeatOnStarted {
            viewModel
                .isLoading
                .onEach { isLoading = it }
                .collectLatest(loadingIndicatorAdapter::isLoading::set)
        }
        viewLifecycleOwner.repeatOnStarted {
            viewModel.noMoreItems.collectLatest { noMoreItems = it }
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
            viewModel
                .isLoading
                .filterNot { it }
                .collectLatest(binding.swipeRefresh::setRefreshing)
        }

        binding.swipeRefresh.setOnRefreshListener { viewModel.onRefresh() }
    }
}
