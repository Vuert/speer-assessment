package com.speer.technologies.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.speer.technologies.presentation.base.viewmodel.BaseViewModel
import com.speer.technologies.utils.extensions.common.unsafeLazy
import com.speer.technologies.utils.extensions.lifecycle.launchOnLifecycleDestroy
import com.speer.technologies.utils.extensions.lifecycle.repeatOnStarted
import com.speer.technologies.utils.extensions.view.createViewModel
import com.speer.technologies.utils.extensions.view.inflateBinding
import com.speer.technologies.utils.view.ThrowableToErrorMessageMapper

/**
 * Base class of all fragments
 */
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> :
    Fragment(),
    BaseScreen<VB, VM> {

    protected var binding: VB? = null

    protected val viewModel: VM by unsafeLazy { createViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflateBinding(inflater, container, false)
        .also { binding = it }
        .also { viewLifecycleOwner.launchOnLifecycleDestroy { binding = null } }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireBinding().let {
            listenViewModelErrors(it)
            onViewBound(it, savedInstanceState)
        }
    }

    private fun listenViewModelErrors(binding: VB) {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorFlow.collect { handleError(binding, it) }
        }
    }

    override fun handleError(binding: VB, throwable: Throwable) {
        Snackbar.make(
            binding.root,
            ThrowableToErrorMessageMapper(resources).map(throwable),
            Snackbar.LENGTH_LONG,
        ).show()
    }

    protected fun requireBinding(): VB = requireNotNull(binding) {
        "Binding is null"
    }
}
