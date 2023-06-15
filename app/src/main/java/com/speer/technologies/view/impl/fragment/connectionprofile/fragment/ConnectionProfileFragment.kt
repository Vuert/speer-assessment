package com.speer.technologies.view.impl.fragment.connectionprofile.fragment

import android.os.Bundle
import android.transition.TransitionManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.speer.technologies.R
import com.speer.technologies.databinding.FragmentConnectionProfileBinding
import com.speer.technologies.presentation.impl.connectionprofile.viewmodel.ConnectionProfileViewModel
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import com.speer.technologies.view.base.BaseFragment
import com.speer.technologies.view.impl.common.user.mapper.ParcelableUserToUserMapper
import com.speer.technologies.view.impl.common.userprofile.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class ConnectionProfileFragment :
    BaseFragment<FragmentConnectionProfileBinding, ConnectionProfileViewModel>() {

    private val navArgs by navArgs<ConnectionProfileFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.init(ParcelableUserToUserMapper().map(navArgs.user))
        }
    }

    override fun onViewBound(
        binding: FragmentConnectionProfileBinding,
        savedInstanceState: Bundle?
    ) {
        initToolbar(binding)
        initSwipeRefresh(binding, viewModel)
        initUserInfoLayout(binding, viewModel)
    }

    private fun initToolbar(binding: FragmentConnectionProfileBinding) {
        binding.layoutUserInfo.apply {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun initSwipeRefresh(
        binding: FragmentConnectionProfileBinding,
        viewModel: ConnectionProfileViewModel
    ) {
        binding.apply {
            layoutUserInfo.appBarLayout.addOnOffsetChangedListener { _, offset ->
                swipeRefresh.isEnabled = offset == 0 || swipeRefresh.isRefreshing
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.isLoading.collectLatest(binding.swipeRefresh::setRefreshing)
        }

        binding.swipeRefresh.setOnRefreshListener { viewModel.onRefresh() }
    }

    private fun initUserInfoLayout(
        binding: FragmentConnectionProfileBinding,
        viewModel: ConnectionProfileViewModel
    ) {
        viewLifecycleOwner.repeatOnStarted {
            viewModel
                .user
                .map { it.value }
                .filterNotNull()
                .collectLatest {
                    TransitionManager.beginDelayedTransition(binding.root)
                    binding.layoutUserInfo.show(it)
                }
        }
    }
}
